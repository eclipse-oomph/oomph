/*
 * Copyright (c) 2014 Eike Stepper (Loehne, Germany) and others.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v2.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v20.html
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

static char* lib = NULL;
static char* cab = NULL;
static char* productTar = NULL;
static BOOL debug = FALSE;

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
      GetProcAddress (GetModuleHandle ("kernel32.dll"), "SetDefaultDllDirectories");
  if (addrSetDefaultDllDirectories)
  {
    addrSetDefaultDllDirectories (LOAD_LIBRARY_SEARCH_SYSTEM32);
  }
}

static char*
convertUTF16ToUTF8 (wchar_t* string)
{
  // First compute the number of bytes needed to represent the UTF-16 string as UTF-8 characters, including room for the terminating null character.
  int length = WideCharToMultiByte (CP_UTF8, 0, string, -1, NULL, 0, NULL, NULL) + 1;

  // Allocate a buffer big enough to hold the result.
  char *result = malloc (sizeof(char) * length);

  // Do the actual conversion and return the result;
  WideCharToMultiByte (CP_UTF8, 0, string, -1, result, length, NULL, NULL);

  return result;
}

static wchar_t*
convertUTF8ToUTF16 (char* string)
{
  // First compute the number of UTF-16 characters needed to represent the string's UTF-8 characters, including room for the terminating null character.
  int length = MultiByteToWideChar (CP_UTF8, 0, string, -1, NULL, 0) + 1;

  // Allocate a buffer big enough to hold the result.
  wchar_t *result = malloc (sizeof(wchar_t) * length);

  // Do the actual conversion and return the result;
  MultiByteToWideChar (CP_UTF8, 0, string, -1, result, length);

  return result;
}

static char*
getTempFile (char* prefix, char* suffix)
{
  char tempFolder[MAX_PATH];
  DWORD dwRetVal = GetTempPath (MAX_PATH, tempFolder);
  if (dwRetVal == 0 || dwRetVal > MAX_PATH)
  {
    return NULL;
  }

  char tempFile[MAX_PATH];
  if (GetTempFileName (tempFolder, prefix, 0, tempFile) == 0)
  {
    return NULL;
  }

  char* result = malloc (strlen (tempFile) + strlen (suffix) + 2);
  result[0] = 0;
  strcat (result, tempFile);
  strcat (result, suffix);

  return result;
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

static char*
browseForFolder (HWND hwndOwner, LPCTSTR lpszTitle)
{
  CoInitialize (NULL);

  char* result = NULL;
  char buffer[MAX_PATH];

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
      result = strdup (buffer);
    }

    CoTaskMemFree (itemIDList);
  }

  // CoUninitialize ();
  return result;
}

static char*
browseForFile (HWND hwndOwner, LPCTSTR lpszTitle, LPCTSTR lpszFilter)
{
  char szFile[MAX_PATH];
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
  char* launcherPath;
  char* iniPath;
  char* productName;
  char* productURI;
  char* imageURI;
} REQ;

static BOOL
findDescriptor (char* executable, REQ* req, BOOL ignoreCab)
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

  wchar_t* wideExecutable = convertUTF8ToUTF16 (executable);
  FILE* file = _wfopen (wideExecutable, L"rb");
  byte in_buffer[4048];
  int in_buffer_pos = 0;
  int in_buffer_size = 0;

  BOOL retcode = FALSE;
  byte b;
  long pos = 0;
  int libdataSize = 60000;
  byte libdata[libdataSize];
  int libdataPos = 0;

  FILE *outFile = NULL;

  int o;
  int markerLimit = 6;
  for (o = 0; o < markerLimit; ++o)
  {
    int k = 0;
    for (;;)
    {
      if (o >= 2)
      {
        // Only start using the buffer after we've read the descriptor because the descriptor processing reads directly from the file.
        if (in_buffer_pos >= in_buffer_size)
        {
          in_buffer_size = fread(in_buffer, 1, 4048, file);
          if (in_buffer_size == 0)
          {
            break;
          }
          in_buffer_pos = 0;
        }

        b = in_buffer[in_buffer_pos++];
      }
      else if (fread (&b, 1, 1, file) == 0)
      {
        break;
      }

      if (o == 1)
      {
        libdata[libdataPos++] = b;
      }
      else if (outFile != NULL)
      {
        libdata[libdataPos++] = b;
        if (libdataPos == libdataSize)
        {
          // Don't write the trailing bytes that might be part of the marker.
          fwrite (libdata, libdataPos - size, 1, outFile);

          // Move the trailing bytes to the start
          int n;
          int m = libdataSize  - size;
          for (n = 0; n < size; ++n, ++m)
          {
            libdata[n] = libdata[m];
          }

          libdataPos = size;
        }
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
          if (debug)
          {
            printf ("Found marker\n");
            fflush (stdout);
          }
        }
        else if (o == 1)
        {
          // We've found the libdata.jar. Skip...
          if (debug)
          {
            printf ("Found libdata.jar\n");
            fflush (stdout);
          }

          // Save the captured libdata.jar bytes to a temporary file.
          lib = getTempFile ("ext", ".jar");

          FILE *fp = _wfopen (convertUTF8ToUTF16 (lib), L"wb");
          fwrite (libdata, libdataPos - size, 1, fp);
          fclose (fp);
          libdataPos = 0;

          // Extract the product descriptor.
          int size = 2048;
          char buffer[size];

          fgets (buffer, size, file);
          sscanf (buffer, "%d", &req->format);

          if (req->format == 1 || (req->format == 2 && ignoreCab))
          {
            markerLimit = 2;
          }

          fgets (buffer, size, file);
          sscanf (buffer, "%d", &req->major);

          fgets (buffer, size, file);
          sscanf (buffer, "%d", &req->minor);

          fgets (buffer, size, file);
          sscanf (buffer, "%d", &req->micro);

          fgets (buffer, size, file);
          sscanf (buffer, "%d", &req->bitness);

          fgets (buffer, size, file);
          sscanf (buffer, "%d", &req->jdk);

          fgets (buffer, size, file);
          req->launcherPath = strdup (strtok (buffer, "\n\r"));

          fgets (buffer, size, file);
          req->iniPath = strdup (strtok (buffer, "\n\r"));

          fgets (buffer, size, file);
          req->productName = strdup (strtok (buffer, "\n\r"));

          fgets (buffer, size, file);
          req->productURI = strdup (strtok (buffer, "\n\r"));

          fgets (buffer, size, file);
          req->imageURI = strdup (strtok (buffer, "\n\r"));

          retcode = TRUE;
        }
        else if (o == 2)
        {
          if (req->format == 3)
          {
            if (debug)
            {
              printf ("Found start of product tar\n");
              fflush (stdout);
            }
            productTar = getTempFile ("product", ".tar");
            outFile = _wfopen (convertUTF8ToUTF16 (productTar), L"wb");
          }
          else
          {
            // We've found the preceding libdata.jar. Skip...
            if (debug)
            {
              printf ("Finished libdata.jar\n");
              fflush (stdout);
            }
          }
        }
        else if (o == 3)
        {
          if (req->format == 3)
          {
            if (debug)
            {
              printf ("Found end of product tar\n");
              fflush (stdout);
            }

            if (libdataPos - size > 0)
            {
              fwrite (libdata, libdataPos - size, 1, outFile);
            }

            fclose (outFile);
          }
          else
          {
            if (debug)
            {
              printf ("Found start of cab\n");
              fflush (stdout);
            }
            cab = getTempFile ("jre", ".cab");
            outFile = _wfopen (convertUTF8ToUTF16 (cab), L"wb");
          }
        }
        else if (o == 4)
        {
          if (req->format == 3)
          {
            if (debug)
            {
              printf ("Found end of cab\n");
              fflush (stdout);
            }

            if (libdataPos - size > 0)
            {
              fwrite (libdata, libdataPos - size, 1, outFile);
            }

            fclose (outFile);
          }
          else
          {
            if (debug)
            {
              printf ("Found end of product tar\n");
              fflush (stdout);
            }

          }
        }
        else
        {
          if (debug)
          {
            printf ("Found %d\n", o);
            fflush (stdout);
          }
        }

        break;
      }

      ++pos;
    }
  }

  fclose (file);
  return retcode;
}

static BOOL
execCommand (char *cmdline, BOOL show, BOOL wait)
{
  if (debug)
  {
    printf ("Executing: %s\n", cmdline);
    fflush (stdout);
  }

  BOOL result = FALSE;

  STARTUPINFOW si;
  PROCESS_INFORMATION pi;

  ZeroMemory(&si, sizeof(si) );
  si.cb = sizeof(si);
  si.dwFlags = STARTF_USESHOWWINDOW;
  si.wShowWindow = show ? SW_SHOWNORMAL : SW_HIDE;

  ZeroMemory(&pi, sizeof(pi) );

  // Start the child process using the UTF-16 command line.
  wchar_t* wideCommandLine = convertUTF8ToUTF16 (cmdline);
  if (!CreateProcessW (NULL,   // No module name (use command line)
      wideCommandLine,        // Command line
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
  if (wait)
  {
    WaitForSingleObject (pi.hProcess, INFINITE);
  }

  DWORD exitCode;
  if (GetExitCodeProcess (pi.hProcess, &exitCode) != FALSE)
  {
    result = exitCode == 0;
  }

  // Close process and thread handles.
  CloseHandle (pi.hProcess);
  CloseHandle (pi.hThread);

  free (wideCommandLine);

  if (!wait)
  {
    return TRUE;
  }

  return result;
}

/****************************************************************************************
 * Java Library Management
 ***************************************************************************************/

static BOOL
execLib (char* javaHome, char *vmargs, char* className, char* args)
{
  size_t cmdlineSize = sizeof(char) * (strlen (javaHome) + (vmargs == NULL ? 0 : strlen (vmargs)) + strlen (className) + strlen (args) + MAX_PATH);

  char* cmdline = malloc (cmdlineSize);

  char* lastDot = strrchr (javaHome, '.');
  if (lastDot != NULL && strcmp (lastDot, ".exe") == 0)
  {
    if (vmargs == NULL)
    {
      if (snprintf (cmdline, cmdlineSize, "\"%s\" -cp \"%s\" %s %s", javaHome, lib, className, args) >= cmdlineSize)
      {
        return FALSE;
      }
    }
    else
    {
      if (snprintf (cmdline, cmdlineSize, "\"%s\" %s -cp \"%s\" %s %s", javaHome, vmargs, lib, className, args) >= cmdlineSize)
      {
        return FALSE;
      }

    }
  }
  else
  {
    if (vmargs == NULL)
    {
      if (snprintf (cmdline, cmdlineSize, "\"%s\\bin\\javaw\" -cp \"%s\" %s %s", javaHome, lib, className, args) >= cmdlineSize)
      {
        return FALSE;
      }
    }
    else
    {
      if (snprintf (cmdline, cmdlineSize, "\"%s\\bin\\javaw\" %s -cp \"%s\" %s %s", javaHome, vmargs, lib, className, args) >= cmdlineSize)
      {
        return FALSE;
      }
    }
  }

  return execCommand (cmdline, FALSE, TRUE);
}

static BOOL
validateJRE (JRE* jre, REQ* req)
{
  if (jre->jdk < req->jdk)
  {
    return FALSE;
  }

  char args[4 * 12];
  if (snprintf (args, sizeof(args), "%d %d %d %d", req->major, req->minor, req->micro, req->bitness) >= sizeof(args))
  {
    return FALSE;
  }

  return execLib (jre->javaHome, NULL, "org.eclipse.oomph.extractor.lib.JREValidator", args);
}

static BOOL
extractProduct (JRE* jre, JRE* argJRE, char* vmargs, char* executable, char* targetFolder, char* productCommandLineArguments)
{
  size_t size = sizeof(char) * (MAX_PATH + strlen (productCommandLineArguments));
  char* args = malloc (size);

  if (argJRE == NULL)
  {
    if (snprintf (args, size, "\"%s\" \"%s\" -- %s", executable, targetFolder, productCommandLineArguments) >= size)
    {
      return FALSE;
    }
  }
  else
  {
    if (snprintf (args, size, "\"%s\" \"%s\" \"%s\" -- %s", executable, targetFolder, argJRE->javaHome, productCommandLineArguments) >= size)
    {
      return FALSE;
    }
  }

  return execLib (jre->javaHome, vmargs, "org.eclipse.oomph.extractor.lib.BINExtractor", args);
}

static char*
getVM (char* path)
{
  char vm[MAX_PATH];
  if (snprintf (vm, sizeof(vm), "%s\\javaw.exe", path) >= sizeof(vm))
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

  char path[32000] = "";
  if (GetEnvironmentVariable ("PATH", path, sizeof(path)) != 0)
  {
    char* token = strtok (path, ";");
    while (token != NULL)
    {
      char* vm = getVM (token);
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

      token = strtok (NULL, ";");
    }

    if (*defaultJRE != NULL)
    {
      (*defaultJRE)->next = jres;
      jres = *defaultJRE;
    }
  }

  return jres;
}

static char*
getProductCommandLineArguments (int argc, char*argv[], BOOL ignoreVM)
{
  // Compute the length of the result.
  // Each argument will be surrounded by quotes and separated by a space, so include room for these three additional characters.
  // Also include room for the terminating null character.
  int length = 1;
  if (!ignoreVM)
  {
    // If there is a -vm, we'll append \bin to it.
    length +=  4;
  }

  int i = 0;
  while (++i < argc)
  {
    char * arg = argv[i];
    length += strlen (arg) + 3;
  }

  // Allocate the result and initialize the bytes.
  char* result = malloc (sizeof(char) * length);
  memset (result, 0, sizeof(char) * length);

  // Compose the arguments.
  i = 0;
  while (++i < argc)
  {
    if (i == 1 && ignoreVM && strcmp ("-vm", argv[1]) == 0)
    {
      // Ignore the VM argument and the value that follows.
      ++i;
    }
    else
    {
      if (strlen (result) == 0)
      {
        strncat (result, "\"", length);
      }
      else
      {
        strncat (result, " \"", length);
      }

      strncat (result, argv[i], length);
      if (i == 2 && strcmp ("-vm", argv[1]) == 0)
      {
        strncat (result, "\\bin", length);
      }

      strncat (result, "\"", length);
    }
  }

  return result;
}

static int
getArgv (char** argv[])
{
  // On Windows, the value of main's argv is not UTF-8 encoded but rather is using the system encoding, e.g., Latin-1.
  // So we'd best get the raw Windows command line via Windows APIs, which uses a UTF-16 encoding.

  // Get the command line using Windows API.
  LPWSTR commandLineW = GetCommandLineW ();

  // Split the command line into wide character (UTF-16) arguments.
  int utf16Argc;
  LPWSTR *utf16Argv = CommandLineToArgvW (commandLineW, &utf16Argc);

  char** utf8Argv = malloc (sizeof(char*) * utf16Argc);

  int i = -1;
  while (++i < utf16Argc)
  {
    LPWSTR utf16Arg = utf16Argv[i];
    utf8Argv[i] = convertUTF16ToUTF8 (utf16Arg);
  }

  *argv = utf8Argv;
  return utf16Argc;
}

/****************************************************************************************
 * Untar
 ***************************************************************************************/

static int
parseoct (const char *p, size_t n)
{
  int i = 0;

  while (*p < '0' || *p > '7')
  {
    ++p;
    --n;
  }

  while (*p >= '0' && *p <= '7' && n > 0)
  {
    i *= 8;
    i += *p - '0';
    ++p;
    --n;
  }

  return i;
}

static BOOL
isEndOfArchive (const char *p)
{
  int n;
  for (n = 511; n >= 0; --n)
  {
    if (p[n] != '\0')
    {
      return 0;
    }
  }

  return 1;
}

static char*
getAbsolutePath (char* pathname, char* targetDir)
{
  size_t size = strlen (pathname) + strlen (targetDir) + 3;
  char * result = malloc (sizeof(char) * size);
  result[0] = 0;
  strcat (result, targetDir);
  strcat (result, pathname);
  char*p = result;
  while (*p != 0)
  {
    if (*p == '/')
    {
      *p = '\\';
    }
    ++p;
  }

  return result;
}

static void
createDirectory (char *pathname, char *targetDir)
{
  char *p;
  int r;

  // Strip trailing '/'
  if (pathname[strlen (pathname) - 1] == '/')
  {
    pathname[strlen (pathname) - 1] = '\0';
  }

  char* path = getAbsolutePath (pathname, targetDir);

  // Try creating the directory.
  r = mkdir (path);

  if (r != 0)
  {
    // try creating parent directory.
    p = strrchr (pathname, '/');
    if (p != NULL)
    {
      *p = '\0';
      createDirectory (pathname, targetDir);
      *p = '/';
      r = mkdir (path);
    }
  }

  if (r != 0)
  {
    fprintf (stderr, "Could not create directory %s\n", path);
    fflush (stderr);
  }
}

static FILE *
createFile (char *pathname, char * targetDir)
{
  FILE *f;

  char* path = getAbsolutePath (pathname, targetDir);
  f = fopen (path, "wb");
  if (f == NULL)
  {
    // Try creating parent directory and then creating file.
    char *p = strrchr (pathname, '/');
    if (p != NULL)
    {
      *p = '\0';
      createDirectory (pathname, targetDir);
      *p = '/';
      f = fopen (path, "wb");
    }
  }

  return f;
}

static int
verifyChecksum (const char *p)
{
  int n, u = 0;
  for (n = 0; n < 512; ++n)
  {
    if (n < 148 || n > 155)
    {
      // The standard tar checksum adds unsigned bytes.
      u += ((unsigned char *) p)[n];
    }
    else
    {
      u += 0x20;
    }
  }

  return u == parseoct (p + 148, 8);
}

static void
untar (FILE *a, char *path, char* targetDir)
{
  char buff[512];
  FILE *f = NULL;
  size_t bytes_read;
  int filesize;

  if (debug)
  {
    printf ("Extracting from %s to %s\n", path, targetDir);
    fflush (stdout);
  }

  char *longLink = NULL;
  char *longLinkOffset = NULL;

  for (;;)
  {
    bytes_read = fread (buff, 1, 512, a);

    if (bytes_read < 512)
    {
      fprintf (stderr, "Short read on %s: expected 512, got %lu\n", path, (unsigned long) bytes_read);
      fflush (stderr);
      return;
    }

    if (isEndOfArchive (buff))
    {
      if (debug)
      {
        printf ("End of %s\n", path);
        fflush (stdout);
      }

      return;
    }

    if (!verifyChecksum (buff))
    {
      fprintf (stderr, "Checksum failure\n");
      fflush (stderr);
      return;
    }

    // File modes don't matter on Windows.
    buff[100] = 0;

    filesize = parseoct (buff + 124, 12);
    switch (buff[156])
    {
      case '1':
        if (debug)
        {
          printf (" Ignoring hardlink %s\n", buff);
          fflush (stdout);
        }
        break;
      case '2':
        if (debug)
        {
          printf (" Ignoring symlink %s\n", buff);
          fflush (stdout);
        }
        break;
      case '3':
        if (debug)
        {
          printf (" Ignoring character device %s\n", buff);
          fflush (stdout);
        }
        break;
      case '4':
        if (debug)
        {
          printf (" Ignoring block device %s\n", buff);
          fflush (stdout);
        }
        break;
      case '5':
        if (longLink != NULL)
        {
          if (debug)
          {
            printf (" Extracting long directory %s\n", longLink);
            fflush (stdout);
          }

          createDirectory (longLink, targetDir);
          longLink = NULL;
          longLinkOffset = NULL;
        }
        else
        {
          if (debug)
          {
            printf (" Extracting directory %s\n", buff);
            fflush (stdout);
          }

          createDirectory (buff, targetDir);
        }

        filesize = 0;
        break;
      case '6':
        if (debug)
        {
          printf (" Ignoring FIFO %s\n", buff);
          fflush (stdout);
        }
        break;
      default:
        if (strcmp(buff, "././@LongLink") == 0)
        {
          if (debug)
          {
            printf (" Reading long link %s\n", buff);
            fflush (stdout);
          }

          longLink = malloc(filesize + 1);
          longLinkOffset = longLink;
        }
        else if (longLink != NULL)
        {
          if (debug)
          {
            printf (" Extracting long file %s\n", longLink);
            fflush (stdout);
          }

          f = createFile (longLink, targetDir);
          longLink = NULL;
          longLinkOffset = NULL;
        }
        else
        {
          if (debug)
          {
            printf (" Extracting file %s\n", buff);
            fflush (stdout);
          }

          f = createFile (buff, targetDir);
        }
        break;
    }

    while (filesize > 0)
    {
      bytes_read = fread (buff, 1, 512, a);
      if (bytes_read < 512)
      {
        fprintf (stderr, "Short read on %s: Expected 512, got %lu\n", path, (unsigned long) bytes_read);
        fflush (stderr);
        return;
      }

      if (filesize < 512)
      {
        bytes_read = filesize;
      }

      if (f != NULL)
      {
        if (fwrite (buff, 1, bytes_read, f) != bytes_read)
        {
          fprintf (stderr, "Failed write\n");
          fflush (stderr);
          fclose (f);
          f = NULL;
        }
      }
      else if (longLinkOffset != NULL)
      {
        int i;
        for (i = 0; i < bytes_read; ++i)
        {
          longLinkOffset[i] = buff[i];
        }

        longLinkOffset += bytes_read;
        longLinkOffset[0] = 0;
      }

      filesize -= bytes_read;
    }

    if (f != NULL)
    {
      fclose (f);
      f = NULL;
    }
  }
}

static BOOL
extractTar (char * tarFile, char *targetDir, BOOL createRoot)
{
  char root[2];
  root[0] = '/';
  root[1] = 0;

  if (createRoot)
  {
    createDirectory (root, targetDir);
  }

  FILE *a = fopen (tarFile, "rb");
  if (a == NULL)
  {
    fprintf (stderr, "Unable to open %s\n", tarFile);
    fflush (stderr);
    return 1;
  }
  else
  {
    untar (a, tarFile, targetDir);
    fclose (a);
    return 0;
  }
}

/****************************************************************************************
 * Main Entry Point
 *
 * On Windows the argv for the standard main function is not UTF-8 encoded rather is encoded using the system encoding, e.g., Latin-1.
 * So it's best to use getArgv to convert the UTF-16 command line arguments to UTF-8.
 ***************************************************************************************/

int
main (int argcIgnored, char** argvIngored)
{
  protectAgainstDLLHijacking ();

  char** argv;
  int argc = getArgv (&argv);

  if (argc > 1 && strcmp (argv[1], "--debug") == 0)
  {
    debug = TRUE;

    // Attach to the console and redirect to it.
    if (AttachConsole(ATTACH_PARENT_PROCESS))
    {
      freopen("CONIN$", "r",stdin);
      freopen("CONOUT$", "w",stdout);
      freopen("CONOUT$", "w",stderr);
    }

    // Shift the args to remove this one.
    --argc;
    {
      int i;
      for (i = 1; i < argc; ++i)
      {
        argv[i] = argv[i + 1];
      }
    }
  }

  BOOL validateJREs = TRUE;
  char* explicitJRE = NULL;
  if (argc > 1)
  {
    char* option = argv[1];
    if (strcmp (option, "-web") == 0)
    {
      validateJREs = FALSE;
    }
    else if (strcmp (option, "-vm") == 0 && argc > 2)
    {
      explicitJRE = argv[2];
    }
  }

  char* executable = argv[0];
  REQ req;

  if (!findDescriptor (executable, &req, explicitJRE != NULL))
  {
    fprintf (stderr, "No product descriptor\n");
    fflush (stderr);
    return EXIT_FAILURE_PRODUCT_DESCRIPTION;
  }

  char sysdir[MAX_PATH];
  if (!GetSystemDirectory (sysdir, sizeof(sysdir)))
  {
    fprintf (stderr, "No system directory\n");
    fflush (stderr);
    return EXIT_FAILURE_SYSTEM_DIRECTORY;
  }

  if (productTar != NULL)
  {
    char* targetFolder = getTempFile ("eoi", "");
    if (targetFolder == NULL)
    {
      char label[MAX_PATH];
      if (snprintf (label, sizeof(label), "Extract %s to:", req.productName) >= sizeof(label))
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

    char *targetFolderWithSlash = malloc (strlen (targetFolder) + 1);
    targetFolderWithSlash[0] = 0;
    strcat (targetFolderWithSlash, targetFolder);
    strcat (targetFolderWithSlash, "\\");

    extractTar(productTar, targetFolderWithSlash, TRUE);

    char* productCommandLineArguments = getProductCommandLineArguments (argc, argv, FALSE);
    char *command = malloc(strlen(sysdir) + strlen(targetFolderWithSlash) + strlen(req.launcherPath) +  strlen(productCommandLineArguments) + 10);
    command[0] = 0;
    strcat(command, "\"");
    strcat(command, targetFolderWithSlash);
    strcat(command, req.launcherPath);
    strcat(command, "\"");
    strcat(command, " ");
    strcat(command, productCommandLineArguments);

    execCommand(command, TRUE, FALSE);

    return EXIT_SUCCESS;
  }

  char * tarFile = NULL;
  if (cab != NULL && validateJREs)
  {
    if (debug)
    {
      printf ("CabFile in %s\n", cab);
      fflush (stdout);
    }

    char * targetFolder = malloc (strlen (cab) + 10);
    targetFolder[0] = 0;
    strcat (targetFolder, cab);
    strcat (targetFolder, ".jre");

    char root[2];
    root[0] = '/';
    root[1] = 0;
    createDirectory (root, targetFolder);

    if (debug)
    {
      printf ("targetFolder=%s\n", targetFolder);
      fflush (stdout);
    }

    char expand[4 * MAX_PATH];
    if (snprintf (expand, sizeof(expand), "\"%s\\expand.exe\" -r \"%s\" \"%s\"", sysdir, cab, targetFolder) >= sizeof(expand))
    {
      return EXIT_FAILURE_BUFFER_OVERFLOW;
    }

    execCommand (expand, FALSE, TRUE);

    tarFile = malloc (strlen (targetFolder) + 30);
    tarFile[0] = 0;
    strcat (tarFile, targetFolder);
    strcat (tarFile, "\\jre.tar");
    strcat (targetFolder, "\\");
    extractTar (tarFile, targetFolder, FALSE);
    explicitJRE = malloc (strlen (targetFolder) + 30);
    explicitJRE[0] = 0;
    strcat (explicitJRE, targetFolder);
    strcat (explicitJRE, "\\jre");
  }

  if (validateJREs)
  {
    JRE* defaultJRE = NULL;
    JRE* jre = NULL;
    JRE* validatedJRE = NULL;

    if (explicitJRE != NULL)
    {
      jre = malloc (sizeof(JRE));
      jre->javaHome = explicitJRE;
      jre->jdk = 0;
      jre->next = NULL;

      if (!validateJRE (jre, &req))
      {
        char message[400 + MAX_PATH];
        if (snprintf (message, sizeof(message), "The required %d-bit Java %d.%d.%d virtual machine could not be found at the following location: '%s'",
                      req.bitness, req.major, req.minor, req.micro, explicitJRE) >= sizeof(message))
        {
          return EXIT_FAILURE_BUFFER_OVERFLOW;
        }

        MessageBox (NULL, message, "Eclipse Installer", MB_OK | MB_ICONERROR);
        return EXIT_CANCEL;
      }

      validatedJRE = jre;
    }
    else
    {
      jre = findAllJREsAndVMs (&defaultJRE);
    }

    if (jre == NULL)
    {
      char message[400];
      if (snprintf (message, sizeof(message),
                    "The required %d-bit Java %d.%d.%d virtual machine could not be found.\nDo you want to browse your system for it?", req.bitness, req.major,
                    req.minor, req.micro) >= sizeof(message))
      {
        return EXIT_FAILURE_BUFFER_OVERFLOW;
      }

      if (MessageBox (NULL, message, "Eclipse Installer", MB_YESNO | MB_ICONQUESTION) == IDYES)
      {
        char label[100];
        if (snprintf (label, sizeof(label), "Select a %d-Bit Java %d.%d.%d Virtual Machine", req.bitness, req.major, req.minor, req.micro) >= sizeof(label))
        {
          return EXIT_FAILURE_BUFFER_OVERFLOW;
        }

        char* vm = browseForFile (NULL, label, "javaw.exe\0javaw.exe\0\0");
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
      if (jre == validatedJRE || validateJRE (jre, &req))
      {
        char* targetFolder = getTempFile ("eoi", "");
        if (targetFolder == NULL)
        {
          char label[MAX_PATH];
          if (snprintf (label, sizeof(label), "Extract %s to:", req.productName) >= sizeof(label))
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

        if (tarFile != NULL)
        {
          char *jreTargetFolder = malloc (MAX_PATH);
          jreTargetFolder[0] = 0;
          strcat (jreTargetFolder, targetFolder);
          strcat (jreTargetFolder, "\\");
          extractTar (tarFile, jreTargetFolder, TRUE);
          free (jreTargetFolder);
        }

        char* productCommandLineArguments = getProductCommandLineArguments (argc, argv, TRUE);
        JRE* argJRE = tarFile != NULL || jre == defaultJRE ? NULL : jre;
        if (extractProduct (jre, argJRE, (debug ? "-Dorg.eclipse.oomph.extractor.lib.BINExtractor.log=true" : NULL), executable, targetFolder,
                            productCommandLineArguments))
        {
          return EXIT_SUCCESS;
        }

        return EXIT_FAILURE_PRODUCT_EXTRACTION;
      }

      jre = jre->next;
    }
  }

  char url[4 * MAX_PATH];
  if (snprintf (url, sizeof(url),
                "\"%s\\rundll32.exe\" %s\\url.dll,FileProtocolHandler \"http://download.eclipse.org/oomph/jre/?vm=1_%d_%d_%d_%d_%d&pn=%s&pu=%s&pi=%s\"", //
                sysdir, sysdir, req.major, req.minor, req.micro, req.bitness, req.jdk, req.productName, req.productURI, req.imageURI) >= sizeof(url))
  {
    return EXIT_FAILURE_BUFFER_OVERFLOW;
  }

  WinExec (url, SW_HIDE);
  return EXIT_FAILURE_JRE_DETECTION;
}
