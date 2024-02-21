/* DO NOT EDIT THIS FILE - it is machine generated */
#include <jni.h>
/* Header for class scalallvm_LLVM_00024 */

#ifndef _Included_scalallvm_LLVM_00024
#define _Included_scalallvm_LLVM_00024
#ifdef __cplusplus
extern "C" {
#endif
/*
 * Class:      scalallvm_LLVM_00024
 * Method:     ContextCreate
 * Signature:  ()J
 */
JNIEXPORT jlong JNICALL Java_scalallvm_LLVM_00024_ContextCreate
  (JNIEnv *, jobject);

/*
 * Class:      scalallvm_LLVM_00024
 * Method:     ContextDispose
 * Signature:  (J)V
 */
JNIEXPORT void JNICALL Java_scalallvm_LLVM_00024_ContextDispose
  (JNIEnv *, jobject, jlong);

/*
 * Class:      scalallvm_LLVM_00024
 * Method:     ModuleCreateWithNameInContext
 * Signature:  (Ljava/lang/String;J)J
 */
JNIEXPORT jlong JNICALL Java_scalallvm_LLVM_00024_ModuleCreateWithNameInContext
  (JNIEnv *, jobject, jstring, jlong);

/*
 * Class:      scalallvm_LLVM_00024
 * Method:     ModuleDescription
 * Signature:  (J)Ljava/lang/String;
 */
JNIEXPORT jstring JNICALL Java_scalallvm_LLVM_00024_ModuleDescription
  (JNIEnv *, jobject, jlong);

/*
 * Class:      scalallvm_LLVM_00024
 * Method:     ModuleGetName
 * Signature:  (J)Ljava/lang/String;
 */
JNIEXPORT jstring JNICALL Java_scalallvm_LLVM_00024_ModuleGetName
  (JNIEnv *, jobject, jlong);

/*
 * Class:      scalallvm_LLVM_00024
 * Method:     ModuleDispose
 * Signature:  (J)V
 */
JNIEXPORT void JNICALL Java_scalallvm_LLVM_00024_ModuleDispose
  (JNIEnv *, jobject, jlong);

/*
 * Class:      scalallvm_LLVM_00024
 * Method:     ModuleSetName
 * Signature:  (JLjava/lang/String;)V
 */
JNIEXPORT void JNICALL Java_scalallvm_LLVM_00024_ModuleSetName
  (JNIEnv *, jobject, jlong, jstring);

/*
 * Class:      scalallvm_LLVM_00024
 * Method:     TypeDescription
 * Signature:  (JZ)Ljava/lang/String;
 */
JNIEXPORT jstring JNICALL Java_scalallvm_LLVM_00024_TypeDescription
  (JNIEnv *, jobject, jlong, jboolean);

/*
 * Class:      scalallvm_LLVM_00024
 * Method:     TypeGetKind
 * Signature:  (J)B
 */
JNIEXPORT jbyte JNICALL Java_scalallvm_LLVM_00024_TypeGetKind
  (JNIEnv *, jobject, jlong);

/*
 * Class:      scalallvm_LLVM_00024
 * Method:     ArrayTypeCreateInContext
 * Signature:  (IJJ)J
 */
JNIEXPORT jlong JNICALL Java_scalallvm_LLVM_00024_ArrayTypeCreateInContext
  (JNIEnv *, jobject, jint, jlong, jlong);

/*
 * Class:      scalallvm_LLVM_00024
 * Method:     ArrayTypeCount
 * Signature:  (J)I
 */
JNIEXPORT jint JNICALL Java_scalallvm_LLVM_00024_ArrayTypeCount
  (JNIEnv *, jobject, jlong);

/*
 * Class:      scalallvm_LLVM_00024
 * Method:     ArrayTypeElement
 * Signature:  (J)J
 */
JNIEXPORT jlong JNICALL Java_scalallvm_LLVM_00024_ArrayTypeElement
  (JNIEnv *, jobject, jlong);

/*
 * Class:      scalallvm_LLVM_00024
 * Method:     ArrayTypeKind
 * Signature:  ()B
 */
JNIEXPORT jbyte JNICALL Java_scalallvm_LLVM_00024_ArrayTypeKind
  (JNIEnv *, jobject);

/*
 * Class:      scalallvm_LLVM_00024
 * Method:     DoubleTypeInContext
 * Signature:  (J)J
 */
JNIEXPORT jlong JNICALL Java_scalallvm_LLVM_00024_DoubleTypeInContext
  (JNIEnv *, jobject, jlong);

/*
 * Class:      scalallvm_LLVM_00024
 * Method:     DoubleTypeKind
 * Signature:  ()B
 */
JNIEXPORT jbyte JNICALL Java_scalallvm_LLVM_00024_DoubleTypeKind
  (JNIEnv *, jobject);

/*
 * Class:      scalallvm_LLVM_00024
 * Method:     FloatTypeInContext
 * Signature:  (J)J
 */
JNIEXPORT jlong JNICALL Java_scalallvm_LLVM_00024_FloatTypeInContext
  (JNIEnv *, jobject, jlong);

/*
 * Class:      scalallvm_LLVM_00024
 * Method:     FloatTypeKind
 * Signature:  ()B
 */
JNIEXPORT jbyte JNICALL Java_scalallvm_LLVM_00024_FloatTypeKind
  (JNIEnv *, jobject);

/*
 * Class:      scalallvm_LLVM_00024
 * Method:     FunctionType
 * Signature:  ([JJ)J
 */
JNIEXPORT jlong JNICALL Java_scalallvm_LLVM_00024_FunctionType
  (JNIEnv *, jobject, jlongArray, jlong);

/*
 * Class:      scalallvm_LLVM_00024
 * Method:     FunctionTypeKind
 * Signature:  ()B
 */
JNIEXPORT jbyte JNICALL Java_scalallvm_LLVM_00024_FunctionTypeKind
  (JNIEnv *, jobject);

/*
 * Class:      scalallvm_LLVM_00024
 * Method:     FunctionTypeParameterAt
 * Signature:  (JI)J
 */
JNIEXPORT jlong JNICALL Java_scalallvm_LLVM_00024_FunctionTypeParameterAt
  (JNIEnv *, jobject, jlong, jint);

/*
 * Class:      scalallvm_LLVM_00024
 * Method:     FunctionTypeParameterCount
 * Signature:  (J)I
 */
JNIEXPORT jint JNICALL Java_scalallvm_LLVM_00024_FunctionTypeParameterCount
  (JNIEnv *, jobject, jlong);

/*
 * Class:      scalallvm_LLVM_00024
 * Method:     FunctionTypeReturn
 * Signature:  (J)J
 */
JNIEXPORT jlong JNICALL Java_scalallvm_LLVM_00024_FunctionTypeReturn
  (JNIEnv *, jobject, jlong);

/*
 * Class:      scalallvm_LLVM_00024
 * Method:     IntTypeInContext
 * Signature:  (IJ)J
 */
JNIEXPORT jlong JNICALL Java_scalallvm_LLVM_00024_IntTypeInContext
  (JNIEnv *, jobject, jint, jlong);

/*
 * Class:      scalallvm_LLVM_00024
 * Method:     IntegerTypeKind
 * Signature:  ()B
 */
JNIEXPORT jbyte JNICALL Java_scalallvm_LLVM_00024_IntegerTypeKind
  (JNIEnv *, jobject);

/*
 * Class:      scalallvm_LLVM_00024
 * Method:     PointerTypeInContext
 * Signature:  (IJ)J
 */
JNIEXPORT jlong JNICALL Java_scalallvm_LLVM_00024_PointerTypeInContext
  (JNIEnv *, jobject, jint, jlong);

/*
 * Class:      scalallvm_LLVM_00024
 * Method:     PointerTypeKind
 * Signature:  ()B
 */
JNIEXPORT jbyte JNICALL Java_scalallvm_LLVM_00024_PointerTypeKind
  (JNIEnv *, jobject);

/*
 * Class:      scalallvm_LLVM_00024
 * Method:     PointerTypeGetAddressSpace
 * Signature:  (J)I
 */
JNIEXPORT jint JNICALL Java_scalallvm_LLVM_00024_PointerTypeGetAddressSpace
  (JNIEnv *, jobject, jlong);

/*
 * Class:      scalallvm_LLVM_00024
 * Method:     StructTypeCreateNominalInContext
 * Signature:  (Ljava/lang/String;[JZJ)J
 */
JNIEXPORT jlong JNICALL Java_scalallvm_LLVM_00024_StructTypeCreateNominalInContext
  (JNIEnv *, jobject, jstring, jlongArray, jboolean, jlong);

/*
 * Class:      scalallvm_LLVM_00024
 * Method:     StructTypeCreateStructuralInContext
 * Signature:  ([JZJ)J
 */
JNIEXPORT jlong JNICALL Java_scalallvm_LLVM_00024_StructTypeCreateStructuralInContext
  (JNIEnv *, jobject, jlongArray, jboolean, jlong);

/*
 * Class:      scalallvm_LLVM_00024
 * Method:     StructTypeGetName
 * Signature:  (J)Ljava/lang/String;
 */
JNIEXPORT jstring JNICALL Java_scalallvm_LLVM_00024_StructTypeGetName
  (JNIEnv *, jobject, jlong);

/*
 * Class:      scalallvm_LLVM_00024
 * Method:     StructTypeIsLiteral
 * Signature:  (J)Z
 */
JNIEXPORT jboolean JNICALL Java_scalallvm_LLVM_00024_StructTypeIsLiteral
  (JNIEnv *, jobject, jlong);

/*
 * Class:      scalallvm_LLVM_00024
 * Method:     StructTypeIsOpaque
 * Signature:  (J)Z
 */
JNIEXPORT jboolean JNICALL Java_scalallvm_LLVM_00024_StructTypeIsOpaque
  (JNIEnv *, jobject, jlong);

/*
 * Class:      scalallvm_LLVM_00024
 * Method:     StructTypeIsPacked
 * Signature:  (J)Z
 */
JNIEXPORT jboolean JNICALL Java_scalallvm_LLVM_00024_StructTypeIsPacked
  (JNIEnv *, jobject, jlong);

/*
 * Class:      scalallvm_LLVM_00024
 * Method:     StructTypeKind
 * Signature:  ()B
 */
JNIEXPORT jbyte JNICALL Java_scalallvm_LLVM_00024_StructTypeKind
  (JNIEnv *, jobject);

/*
 * Class:      scalallvm_LLVM_00024
 * Method:     StructTypeMemberAt
 * Signature:  (JI)J
 */
JNIEXPORT jlong JNICALL Java_scalallvm_LLVM_00024_StructTypeMemberAt
  (JNIEnv *, jobject, jlong, jint);

/*
 * Class:      scalallvm_LLVM_00024
 * Method:     StructTypeMemberCount
 * Signature:  (J)I
 */
JNIEXPORT jint JNICALL Java_scalallvm_LLVM_00024_StructTypeMemberCount
  (JNIEnv *, jobject, jlong);

/*
 * Class:      scalallvm_LLVM_00024
 * Method:     VoidTypeInContext
 * Signature:  (J)J
 */
JNIEXPORT jlong JNICALL Java_scalallvm_LLVM_00024_VoidTypeInContext
  (JNIEnv *, jobject, jlong);

/*
 * Class:      scalallvm_LLVM_00024
 * Method:     VoidTypeKind
 * Signature:  ()B
 */
JNIEXPORT jbyte JNICALL Java_scalallvm_LLVM_00024_VoidTypeKind
  (JNIEnv *, jobject);

/*
 * Class:      scalallvm_LLVM_00024
 * Method:     ValueDescription
 * Signature:  (JZ)Ljava/lang/String;
 */
JNIEXPORT jstring JNICALL Java_scalallvm_LLVM_00024_ValueDescription
  (JNIEnv *, jobject, jlong, jboolean);

/*
 * Class:      scalallvm_LLVM_00024
 * Method:     ValueGetType
 * Signature:  (J)J
 */
JNIEXPORT jlong JNICALL Java_scalallvm_LLVM_00024_ValueGetType
  (JNIEnv *, jobject, jlong);

/*
 * Class:      scalallvm_LLVM_00024
 * Method:     ConstantAggregateMemberAt
 * Signature:  (JI)J
 */
JNIEXPORT jlong JNICALL Java_scalallvm_LLVM_00024_ConstantAggregateMemberAt
  (JNIEnv *, jobject, jlong, jint);

/*
 * Class:      scalallvm_LLVM_00024
 * Method:     ConstantNullValue
 * Signature:  (J)J
 */
JNIEXPORT jlong JNICALL Java_scalallvm_LLVM_00024_ConstantNullValue
  (JNIEnv *, jobject, jlong);

/*
 * Class:      scalallvm_LLVM_00024
 * Method:     ConstantIntCreate
 * Signature:  (JJZ)J
 */
JNIEXPORT jlong JNICALL Java_scalallvm_LLVM_00024_ConstantIntCreate
  (JNIEnv *, jobject, jlong, jlong, jboolean);

/*
 * Class:      scalallvm_LLVM_00024
 * Method:     ConstantIntCreateWide
 * Signature:  (JI[JZ)J
 */
JNIEXPORT jlong JNICALL Java_scalallvm_LLVM_00024_ConstantIntCreateWide
  (JNIEnv *, jobject, jlong, jint, jlongArray, jboolean);

/*
 * Class:      scalallvm_LLVM_00024
 * Method:     ConstantIntGetSExtValue
 * Signature:  (J)J
 */
JNIEXPORT jlong JNICALL Java_scalallvm_LLVM_00024_ConstantIntGetSExtValue
  (JNIEnv *, jobject, jlong);

/*
 * Class:      scalallvm_LLVM_00024
 * Method:     ConstantIntGetZExtValue
 * Signature:  (J)J
 */
JNIEXPORT jlong JNICALL Java_scalallvm_LLVM_00024_ConstantIntGetZExtValue
  (JNIEnv *, jobject, jlong);

/*
 * Class:      scalallvm_LLVM_00024
 * Method:     ConstantDouble
 * Signature:  (JD)J
 */
JNIEXPORT jlong JNICALL Java_scalallvm_LLVM_00024_ConstantDouble
  (JNIEnv *, jobject, jlong, jdouble);

/*
 * Class:      scalallvm_LLVM_00024
 * Method:     ConstantStruct
 * Signature:  (J[J)J
 */
JNIEXPORT jlong JNICALL Java_scalallvm_LLVM_00024_ConstantStruct
  (JNIEnv *, jobject, jlong, jlongArray);

/*
 * Class:      scalallvm_LLVM_00024
 * Method:     ConstantUndefined
 * Signature:  (J)J
 */
JNIEXPORT jlong JNICALL Java_scalallvm_LLVM_00024_ConstantUndefined
  (JNIEnv *, jobject, jlong);

#ifdef __cplusplus
}
#endif
#endif
