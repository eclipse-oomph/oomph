/*
 * Copyright (c) 2014 Eike Stepper (Berlin, Germany) and others.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 *    Eike Stepper - initial API and implementation
 */

#include <io.h>
#include <stdio.h>
#include <stdlib.h>
#include <windows.h>
#include <shlobj.h>
#include <jreinfo.h>

#include "extractor.h"
#include "resources.h"

static _TCHAR* lib = NULL;

// See https://de.wikipedia.org/wiki/DLL_Hijacking
static void
protectAgainstDLLHijacking ()
{
  // The following call is not supported by MinGW's libkernel32.a library:
  // SetDefaultDllDirectories (LOAD_LIBRARY_SEARCH_SYSTEM32);

  // So use GetProcAddress() to call that function dynamically.
  // From libloaderapi.h: WINBASEAPI WINBOOL WINAPI SetDefaultDllDirectories (DWORD DirectoryFlags);
  typedef WINBOOL
  (CALLBACK*AddrSetDefaultDllDirectories) (DWORD);

  AddrSetDefaultDllDirectories addrSetDefaultDllDirectories = (AddrSetDefaultDllDirectories) //
      GetProcAddress (GetModuleHandle (_T("kernel32.dll")), _T("SetDefaultDllDirectories"));
  if (addrSetDefaultDllDirectories)
  {
    addrSetDefaultDllDirectories (LOAD_LIBRARY_SEARCH_SYSTEM32);
  }
}

static _TCHAR*
getTempFile (_TCHAR* prefix)
{
  _TCHAR tempFolder[MAX_PATH];
  DWORD dwRetVal = GetTempPath (MAX_PATH, tempFolder);
  if (dwRetVal == 0 || dwRetVal > MAX_PATH)
  {
    return NULL;
  }

  _TCHAR tempFile[MAX_PATH];
  if (GetTempFileName (tempFolder, prefix, 0, tempFile) == 0)
  {
    return NULL;
  }

  return _tcsdup (tempFile);
}

/****************************************************************************************
 * User Interface
 ***************************************************************************************/

static int CALLBACK
browseCallback (HWND hWnd, UINT uMsg, LPARAM lParam, LPARAM lpData)
{
  if (uMsg == BFFM_INITIALIZED)
  {
    HINSTANCE hInstance = GetModuleHandle (NULL);
    HICON hIcon = LoadIcon (hInstance, MAKEINTRESOURCE(ID_ICON));
    SendMessage (hWnd, WM_SETICON, ICON_SMALL, (LPARAM) hIcon);
  }

  return 0;
}

static _TCHAR*
browseForFolder (HWND hwndOwner, LPCTSTR lpszTitle)
{
  CoInitialize (NULL);

  _TCHAR* result = NULL;
  TCHAR buffer[MAX_PATH];

  BROWSEINFO browseInfo = { 0 };
  browseInfo.hwndOwner = hwndOwner;
  browseInfo.pszDisplayName = buffer;
  browseInfo.pidlRoot = NULL;
  browseInfo.lpszTitle = lpszTitle;
  browseInfo.ulFlags = BIF_RETURNONLYFSDIRS | BIF_USENEWUI;
  browseInfo.lpfn = browseCallback;

  LPITEMIDLIST itemIDList = NULL;
  if ((itemIDList = SHBrowseForFolder (&browseInfo)) != NULL)
  {
    if (SHGetPathFromIDList (itemIDList, buffer))
    {
      result = _tcsdup (buffer);
    }

    CoTaskMemFree (itemIDList);
  }

  // CoUninitialize ();
  return result;
}

static _TCHAR*
browseForFile (HWND hwndOwner, LPCTSTR lpszTitle, LPCTSTR lpszFilter)
{
  _TCHAR szFile[MAX_PATH];
  szFile[0] = 0;

  OPENFILENAME ofn;
  ZeroMemory(&ofn, sizeof(ofn));
  ofn.lStructSize = sizeof(ofn);
  ofn.hwndOwner = hwndOwner;
  ofn.lpstrTitle = lpszTitle;
  ofn.lpstrFile = szFile;
  ofn.nMaxFile = sizeof(szFile);
  ofn.lpstrFilter = lpszFilter;
  ofn.nFilterIndex = 1;
  ofn.lpstrFileTitle = NULL;
  ofn.nMaxFileTitle = 0;
  ofn.lpstrInitialDir = NULL;
  ofn.Flags = OFN_PATHMUSTEXIST | OFN_FILEMUSTEXIST;

  if (GetOpenFileName (&ofn) == TRUE)
  {
    return ofn.lpstrFile;
  }

  return NULL;
}

/****************************************************************************************
 * Product Descriptor Extraction
 ***************************************************************************************/

typedef struct req_s
{
  int format;
  int major;
  int minor;
  int micro;
  int bitness;
  int jdk;
  _TCHAR* launcherPath;
  _TCHAR* iniPath;
  _TCHAR* productName;
  _TCHAR* productURI;
  _TCHAR* imageURI;
} REQ;

static BOOL
findDescriptor (_TCHAR* executable, REQ* req)
{
  marker[0] = 32;
  int size = sizeof(marker);
  int failure[size];
  memset (failure, 0, sizeof(failure));

  int i = 1;
  int j = 0;

  while (i < size)
  {
    while (j > 0 && marker[j] != marker[i])
    {
      j = failure[j - 1];
    }

    if (marker[j] == marker[i])
    {
      ++j;
    }

    failure[i] = j;
    ++i;
  }

  FILE* file = fopen (executable, "rb");
  BOOL retcode = FALSE;
  byte b;
  long pos = 0;
  byte libdata[60000];
  int libdataSize = 0;

  int o;
  for (o = 0; o < 2; ++o)
  {
    int k = 0;
    for (;;)
    {
      if (fread (&b, 1, 1, file) == 0)
      {
        break;
      }

      if (o == 1)
      {
        libdata[libdataSize++] = b;
      }

      while (k > 0 && marker[k] != b)
      {
        k = failure[k - 1];
      }

      if (marker[k] == b)
      {
        ++k;
      }

      if (k == size)
      {
        if (o == 0)
        {
          // We've found the marker that precedes libdata.jar. Skip...
        }
        else if (o == 1)
        {
          // Save the captured libdata.jar bytes to a temporary file.
          lib = getTempFile (_T("ext"));

          FILE *fp = fopen (lib, "wb");
          fwrite (libdata, libdataSize - size, 1, fp);
          fclose (fp);

          // Extract the product descriptor.
          int size = 2048;
          _TCHAR buffer[size];

          fgets (buffer, size, file);
          sscanf (buffer, _T("%d"), &req->format);

          fgets (buffer, size, file);
          sscanf (buffer, _T("%d"), &req->major);

          fgets (buffer, size, file);
          sscanf (buffer, _T("%d"), &req->minor);

          fgets (buffer, size, file);
          sscanf (buffer, _T("%d"), &req->micro);

          fgets (buffer, size, file);
          sscanf (buffer, _T("%d"), &req->bitness);

          fgets (buffer, size, file);
          sscanf (buffer, _T("%d"), &req->jdk);

          fgets (buffer, size, file);
          req->launcherPath = _tcsdup (_tcstok (buffer, _T("\n\r")));

          fgets (buffer, size, file);
          req->iniPath = _tcsdup (_tcstok (buffer, _T("\n\r")));

          fgets (buffer, size, file);
          req->productName = _tcsdup (_tcstok (buffer, _T("\n\r")));

          fgets (buffer, size, file);
          req->productURI = _tcsdup (_tcstok (buffer, _T("\n\r")));

          fgets (buffer, size, file);
          req->imageURI = _tcsdup (_tcstok (buffer, _T("\n\r")));

          retcode = TRUE;
        }

        break;
      }

      if (retcode)
      {
        break;
      }

      ++pos;
    }
  }

  fclose (file);
  return retcode;
}

/****************************************************************************************
 * Java Library Management
 ***************************************************************************************/

static BOOL
execLib (_TCHAR* javaHome, _TCHAR* className, _TCHAR* args)
{
  BOOL result = FALSE;

  _TCHAR cmdline[2 * MAX_PATH];

  _TCHAR* lastDot = strrchr (javaHome, '.');
  if (lastDot != NULL && strcmp (lastDot, _T(".exe")) == 0)
  {
    if (snprintf (cmdline, sizeof(cmdline), _T("\"%s\" -cp \"%s\" %s %s"), javaHome, lib, className, args) >= sizeof(cmdline))
    {
      return FALSE;
    }
  }
  else
  {
    if (snprintf (cmdline, sizeof(cmdline), _T("\"%s\\bin\\javaw\" -cp \"%s\" %s %s"), javaHome, lib, className, args) >= sizeof(cmdline))
    {
      return FALSE;
    }
  }

  STARTUPINFO si;
  PROCESS_INFORMATION pi;

  ZeroMemory(&si, sizeof(si) );
  si.cb = sizeof(si);
  ZeroMemory(&pi, sizeof(pi) );

  // Start the child process.
  if (!CreateProcess (NULL,   // No module name (use command line)
      cmdline,        // Command line
      NULL,           // Process handle not inheritable
      NULL,           // Thread handle not inheritable
      FALSE,          // Set handle inheritance to FALSE
      0,              // No creation flags
      NULL,           // Use parent's environment block
      NULL,           // Use parent's starting directory
      &si,            // Pointer to STARTUPINFO structure
      &pi))           // Pointer to PROCESS_INFORMATION structure
  {
    return FALSE;
  }

  // Wait until child process exits.
  WaitForSingleObject (pi.hProcess, INFINITE);

  DWORD exitCode;
  if (FALSE != GetExitCodeProcess (pi.hProcess, &exitCode))
  {
    result = exitCode == 0;
  }

  // Close process and thread handles.
  CloseHandle (pi.hProcess);
  CloseHandle (pi.hThread);

  //  exitCode = system (cmdline);
  //  result = exitCode == 0;

  //  exitCode = WinExec (cmdline, SW_HIDE);
  //  result= exitCode > 31;

  return result;
}

static BOOL
validateJRE (JRE* jre, REQ* req)
{
  if (jre->jdk < req->jdk)
  {
    return FALSE;
  }

  _TCHAR args[4 * 12];
  if (snprintf (args, sizeof(args), _T("%d %d %d %d"), req->major, req->minor, req->micro, req->bitness) >= sizeof(args))
  {
    return FALSE;
  }

  return execLib (jre->javaHome, _T("org.eclipse.oomph.extractor.lib.JREValidator"), args);
}

static BOOL
extractProduct (JRE* jre, JRE* argJRE, _TCHAR* executable, _TCHAR* targetFolder)
{
  _TCHAR args[MAX_PATH];

  if (argJRE == NULL)
  {
    if (snprintf (args, sizeof(args), _T("\"%s\" \"%s\""), executable, targetFolder) >= sizeof(args))
    {
      return FALSE;
    }
  }
  else
  {
    if (snprintf (args, sizeof(args), _T("\"%s\" \"%s\" \"%s\""), executable, targetFolder, argJRE->javaHome) >= sizeof(args))
    {
      return FALSE;
    }
  }

  return execLib (jre->javaHome, _T("org.eclipse.oomph.extractor.lib.BINExtractor"), args);
}

static _TCHAR*
getVM (_TCHAR* path)
{
  _TCHAR vm[MAX_PATH];
  if (snprintf (vm, sizeof(vm), _T("%s\\javaw.exe"), path) >= sizeof(vm))
  {
    return NULL;
  }

  if ((_access (vm, 0)) != -1)
  {
    return strdup (vm);
  }

  return NULL;

}

static JRE*
findAllJREsAndVMs (JRE** defaultJRE)
{
  JRE* jres = findAllJREs ();

  _TCHAR path[32000] = _T("");
  if (GetEnvironmentVariable (_T("PATH"), path, sizeof(path)) != 0)
  {
    _TCHAR* token = strtok (path, _T(";"));
    while (token != NULL)
    {
      _TCHAR* vm = getVM (token);
      if (vm != NULL)
      {
        JRE* jre = malloc (sizeof(JRE));
        jre->javaHome = vm;
        jre->jdk = 0;

        if (*defaultJRE == NULL)
        {
          *defaultJRE = jre;
        }
        else
        {
          jre->next = jres;
          jres = jre;
        }
      }

      token = strtok (NULL, _T(";"));
    }

    if (*defaultJRE != NULL)
    {
      (*defaultJRE)->next = jres;
      jres = *defaultJRE;
    }
  }

  return jres;
}

/****************************************************************************************
 * Main Entry Point
 ***************************************************************************************/

int
main (int argc, char *argv[])
{
  protectAgainstDLLHijacking ();

  BOOL validateJREs = TRUE;
  if (argc > 1)
  {
    _TCHAR* option = argv[1];
    if (_tcscmp (option, _T("-web")) == 0)
    {
      validateJREs = FALSE;
    }
  }

  _TCHAR* executable = argv[0];
  REQ req;

  if (!findDescriptor (executable, &req))
  {
    printf (_T("No product descriptor\n"));
    return EXIT_FAILURE_PRODUCT_DESCRIPTION;
  }

  if (validateJREs)
  {
    JRE* defaultJRE = NULL;
    JRE* jre = findAllJREsAndVMs (&defaultJRE);

    if (jre == NULL)
    {
      _TCHAR message[400];
      if (snprintf (message, sizeof(message),
                    _T("The required %d-bit Java %d.%d.%d virtual machine could not be found.\nDo you want to browse your system for it?"), req.bitness,
                    req.major, req.minor, req.micro) >= sizeof(message))
      {
        return EXIT_FAILURE_BUFFER_OVERFLOW;
      }

      if (MessageBox (NULL, message, _T("Eclipse Installer"), MB_YESNO | MB_ICONQUESTION) == IDYES)
      {
        _TCHAR label[100];
        if (snprintf (label, sizeof(label), _T("Select a %d-Bit Java %d.%d.%d Virtual Machine"), req.bitness, req.major, req.minor, req.micro) >= sizeof(label))
        {
          return EXIT_FAILURE_BUFFER_OVERFLOW;
        }

        _TCHAR* vm = browseForFile (NULL, label, _T("javaw.exe\0javaw.exe\0\0"));
        if (vm != NULL)
        {
          jre = malloc (sizeof(JRE));
          jre->javaHome = vm;
          jre->jdk = 0;
          jre->next = NULL;
        }
      }
    }

    while (jre)
    {
      if (validateJRE (jre, &req))
      {
        _TCHAR* targetFolder = getTempFile (_T("eoi"));
        if (targetFolder == NULL)
        {
          _TCHAR label[MAX_PATH];
          if (snprintf (label, sizeof(label), _T("Extract %s to:"), req.productName) >= sizeof(label))
          {
            return EXIT_FAILURE_BUFFER_OVERFLOW;
          }

          targetFolder = browseForFolder (NULL, label);
        }
        else
        {
          DeleteFile (targetFolder);
        }

        if (targetFolder == NULL)
        {
          return EXIT_CANCEL;
        }

        JRE* argJRE = jre == defaultJRE ? NULL : jre;
        if (extractProduct (jre, argJRE, executable, targetFolder))
        {
          return EXIT_SUCCESS;
        }

        return EXIT_FAILURE_PRODUCT_EXTRACTION;
      }

      jre = jre->next;
    }
  }

  _TCHAR sysdir[MAX_PATH];
  if (!GetSystemDirectory (sysdir, sizeof(sysdir)))
  {
    return EXIT_FAILURE_SYSTEM_DIRECTORY;
  }

  _TCHAR url[4 * MAX_PATH];
  if (snprintf (url, sizeof(url),
                _T("\"%s\\rundll32.exe\" %s\\url.dll,FileProtocolHandler \"http://download.eclipse.org/oomph/jre/?vm=1_%d_%d_%d_%d_%d&pn=%s&pu=%s&pi=%s\""), //
                sysdir, sysdir, req.major, req.minor, req.micro, req.bitness, req.jdk, req.productName, req.productURI, req.imageURI) >= sizeof(url))
  {
    return EXIT_FAILURE_BUFFER_OVERFLOW;
  }

  WinExec (url, SW_HIDE);
  return EXIT_FAILURE_JRE_DETECTION;
}
