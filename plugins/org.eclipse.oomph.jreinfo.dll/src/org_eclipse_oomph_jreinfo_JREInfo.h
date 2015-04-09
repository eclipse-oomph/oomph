#include <jni.h>
/* Header for class org_eclipse_oomph_jreinfo_JREInfo */

#ifndef _Included_org_eclipse_oomph_jreinfo_JREInfo
#define _Included_org_eclipse_oomph_jreinfo_JREInfo
#ifdef __cplusplus
extern "C" {
#endif

#ifdef BIT32
#define JNI_FUNCTION _Java_org_eclipse_oomph_jreinfo_JREInfo_getAllWin
#else
#define JNI_FUNCTION Java_org_eclipse_oomph_jreinfo_JREInfo_getAllWin
#endif

/*
 * Class:     org_eclipse_oomph_jreinfo_JREInfo
 * Method:    getAllWin
 * Signature: ()Lorg/eclipse/oomph/jreinfo/JREInfo;
 */
JNIEXPORT jobject JNICALL JNI_FUNCTION
  (JNIEnv *, jclass);

#ifdef __cplusplus
}
#endif
#endif
