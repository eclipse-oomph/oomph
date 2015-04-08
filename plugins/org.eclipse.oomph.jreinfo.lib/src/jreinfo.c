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

#include <stdio.h>
#include <stdlib.h>
#include <windows.h>
//#include <shlobj.h>

#include "jreinfo.h"

_TCHAR*
getJavaHome (HKEY key, _TCHAR* subKeyName)
{
  HKEY subKey;
  _TCHAR path[MAX_PATH];
  DWORD length = MAX_PATH;

  if (RegOpenKeyEx (key, subKeyName, 0, KEY_READ, &subKey) == ERROR_SUCCESS)
  {
    if (RegQueryValueEx (subKey, _T("JavaHome"), NULL, NULL, (void*) &path, &length) == ERROR_SUCCESS)
    {
      RegCloseKey (subKey);
      return _tcsdup (path);
    }

    RegCloseKey (subKey);
  }

  return NULL;
}

JRE*
findJRE (JRE* jres, _TCHAR* javaHome)
{
  while (jres)
  {
    if ( _tcscmp (jres->javaHome, javaHome) == 0)
    {
      return jres;
    }

    jres = jres->next;
  }

  return NULL;
}

JRE*
findJREs (JRE* jres, TCHAR* keyName, int jdk)
{
  _TCHAR* javaHome;
  HKEY key;
  DWORD length = MAX_PATH;
  _TCHAR subKeyName[MAX_PATH];

  int j = 0;
  if (RegOpenKeyEx (HKEY_LOCAL_MACHINE, keyName, 0, KEY_READ, &key) == ERROR_SUCCESS)
  {
    while (RegEnumKeyEx (key, j++, subKeyName, &length, NULL, NULL, NULL, NULL) == ERROR_SUCCESS)
    {
      javaHome = getJavaHome (key, subKeyName);
      if (javaHome != NULL && findJRE (jres, javaHome) == NULL)
      {
        JRE* jre = malloc (sizeof(JRE));
        jre->javaHome = javaHome;
        jre->jdk = jdk;
        jre->next = jres;
        jres = jre;
      }

      length = MAX_PATH;
    }

    RegCloseKey (key);
  }

  return jres;
}

JRE*
findJavaHome (JRE* jres)
{
  _TCHAR javaHome[MAX_PATH] = "";
  if (GetEnvironmentVariable ("JAVA_HOME", javaHome, sizeof(javaHome)) != 0)
  {
    if (findJRE (jres, javaHome) == NULL)
    {
      JRE* jre = malloc (sizeof(JRE));
      jre->javaHome = javaHome;
      jre->jdk = 0;
      jre->next = jres;
      jres = jre;
    }
  }

  return jres;
}

JRE*
findAllJREs ()
{
  JRE* jres = NULL;
  jres = findJREs (jres, _T("Software\\Wow6432Node\\JavaSoft\\Java Development Kit"), 1);
  jres = findJREs (jres, _T("Software\\Wow6432Node\\JavaSoft\\Java Runtime Environment"), 0);
  jres = findJREs (jres, _T("Software\\JavaSoft\\Java Development Kit"), 1);
  jres = findJREs (jres, _T("Software\\JavaSoft\\Java Runtime Environment"), 0);
  jres = findJavaHome (jres);
  return jres;
}
