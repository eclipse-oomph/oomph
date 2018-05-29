/*
 * Copyright (c) 2014 Eike Stepper (Loehne, Germany) and others.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 *    Eike Stepper - initial API and implementation
 */

#include <jreinfo.h>

#include "org_eclipse_oomph_jreinfo_JREInfo.h"

JNIEXPORT jobject JNICALL
JNI_FUNCTION (JNIEnv* env, jclass jreinfoClass)
{
  jobject jreinfo = NULL;

  static jfieldID javaHomeID = NULL;
  if (javaHomeID == NULL)
  {
    javaHomeID = (*env)->GetFieldID (env, jreinfoClass, "javaHome", "Ljava/lang/String;");
  }

  static jfieldID jdkID = NULL;
  if (jdkID == NULL)
  {
    jdkID = (*env)->GetFieldID (env, jreinfoClass, "jdk", "I");
  }

  static jfieldID nextID = NULL;
  if (nextID == NULL)
  {
    nextID = (*env)->GetFieldID (env, jreinfoClass, "next", "Lorg/eclipse/oomph/jreinfo/JREInfo;");
  }

  JRE* jre = findAllJREs ();
  while (jre)
  {
    jobject node = (*env)->AllocObject (env, jreinfoClass);

    jstring jniJavaHome = (*env)->NewStringUTF (env, jre->javaHome);
    (*env)->SetObjectField (env, node, javaHomeID, jniJavaHome);

    jint jniJDK = jre->jdk;
    (*env)->SetIntField (env, node, jdkID, jniJDK);

    (*env)->SetObjectField (env, node, nextID, jreinfo);
    jreinfo = node;

    jre = jre->next;
  }

  return jreinfo;
}
