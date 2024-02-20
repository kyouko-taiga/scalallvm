#include <iostream>
#include <llvm/ADT/APInt.h>
#include <llvm/IR/Constants.h>
#include <llvm/IR/LLVMContext.h>
#include <llvm/IR/Module.h>
#include <llvm/Support/raw_ostream.h>

#include "scalallvm_LLVM_00024.h"

/// Converts `p` into a Java handle.
jlong as_handle(void* p) {
  return reinterpret_cast<uintptr_t>(p);
}

/// Unsafely converts the Java handle `h` to a pointer of type `T`.
template<typename T>
T* as_pointer(jlong h) {
  return static_cast<T*>(reinterpret_cast<void*>(h));
}

/// Constructs a llvm::APInt with the given properties.
llvm::APInt arbitrary_precision_integer(
    JNIEnv* e, jint bit_width, jlongArray words, jboolean is_signed
) {
  jlong* w = e->GetLongArrayElements(words, nullptr);
  auto c = e->GetArrayLength(words);
  auto n = bit_width % 64;

  llvm::APInt result((n > 0) ? n : 64, w[c - 1], is_signed);
  for (auto i = c - 2; i >= 0; --i) {
    result = result.concat(llvm::APInt(64, w[c - 1], false));
  }

  e->ReleaseLongArrayElements(words, w, 0);
  return result;
}

JNIEXPORT jlong JNICALL Java_scalallvm_LLVM_00024_ContextCreate(
  JNIEnv*, jobject
) {
  return as_handle(new llvm::LLVMContext());
}

JNIEXPORT void JNICALL Java_scalallvm_LLVM_00024_ContextDispose(
  JNIEnv*, jobject, jlong sh
) {
  delete as_pointer<llvm::LLVMContext>(sh);
}

JNIEXPORT jlong JNICALL Java_scalallvm_LLVM_00024_ModuleCreateWithNameInContext(
  JNIEnv* e, jobject, jstring name, jlong context_h
) {
  const char* n = e->GetStringUTFChars(name, NULL);
  auto* c = as_pointer<llvm::LLVMContext>(context_h);
  return as_handle(new llvm::Module(n, *c));
}

JNIEXPORT jstring JNICALL Java_scalallvm_LLVM_00024_ModuleDescription(
  JNIEnv* e, jobject, jlong sh
) {
  auto* self = as_pointer<llvm::Module>(sh);
  std::string result;
  llvm::raw_string_ostream o(result);
  self->print(o, nullptr);
  o.flush();
  return e->NewStringUTF(result.c_str());
}

JNIEXPORT jstring JNICALL Java_scalallvm_LLVM_00024_ModuleGetName(
  JNIEnv* e, jobject, jlong sh
) {
  auto* self = as_pointer<llvm::Module>(sh);
  return e->NewStringUTF(self->getModuleIdentifier().c_str());
}

JNIEXPORT void JNICALL Java_scalallvm_LLVM_00024_ModuleDispose(
  JNIEnv*, jobject, jlong sh
) {
  delete as_pointer<llvm::Module>(sh);
}

JNIEXPORT void JNICALL Java_scalallvm_LLVM_00024_ModuleSetName(
  JNIEnv* e, jobject, jlong sh, jstring name
) {
  auto* self = as_pointer<llvm::Module>(sh);
  const char* n = e->GetStringUTFChars(name, NULL);
  self->setModuleIdentifier(n);
}

JNIEXPORT jstring JNICALL Java_scalallvm_LLVM_00024_TypeDescription(
  JNIEnv* e, jobject, jlong sh, jboolean is_for_debug
) {
  auto* self = as_pointer<llvm::Type>(sh);
  std::string result;
  llvm::raw_string_ostream o(result);
  self->print(o, is_for_debug);
  o.flush();
  return e->NewStringUTF(result.c_str());
}

JNIEXPORT jlong JNICALL Java_scalallvm_LLVM_00024_IntTypeInContext(
  JNIEnv*, jobject, jint bit_width, jlong context_h
) {
  auto* c = as_pointer<llvm::LLVMContext>(context_h);
  return as_handle(llvm::IntegerType::get(*c, bit_width));
}

JNIEXPORT jlong JNICALL Java_scalallvm_LLVM_00024_PointerTypeInContext(
  JNIEnv*, jobject, jint space, jlong context_h
) {
  auto* c = as_pointer<llvm::LLVMContext>(context_h);
  return as_handle(llvm::PointerType::get(*c, space));
}

JNIEXPORT jint JNICALL Java_scalallvm_LLVM_00024_PointerTypeGetAddressSpace(
  JNIEnv*, jobject, jlong sh
) {
  auto* self = as_pointer<llvm::PointerType>(sh);
  return self->getAddressSpace();
}

JNIEXPORT jlong JNICALL Java_scalallvm_LLVM_00024_VoidTypeInContext(
  JNIEnv*, jobject, jlong context_h
) {
  auto* c = as_pointer<llvm::LLVMContext>(context_h);
  return as_handle(llvm::Type::getVoidTy(*c));
}

JNIEXPORT jstring JNICALL Java_scalallvm_LLVM_00024_ValueDescription(
  JNIEnv* e, jobject, jlong sh, jboolean is_for_debug
) {
  auto* self = as_pointer<llvm::Value>(sh);
  std::string result;
  llvm::raw_string_ostream o(result);
  self->print(o, is_for_debug);
  o.flush();
  return e->NewStringUTF(result.c_str());
}

JNIEXPORT jlong JNICALL Java_scalallvm_LLVM_00024_ValueGetType(
  JNIEnv*, jobject, jlong sh
) {
  auto* self = as_pointer<llvm::Value>(sh);
  return as_handle(self->getType());
}

JNIEXPORT jlong JNICALL Java_scalallvm_LLVM_00024_ConstantNullValue(
  JNIEnv*, jobject, jlong type_h
) {
  auto* t = as_pointer<llvm::Type>(type_h);
  return as_handle(llvm::Constant::getNullValue(t));
}

JNIEXPORT jlong JNICALL Java_scalallvm_LLVM_00024_ConstantIntCreate(
  JNIEnv*, jobject, jlong type_h, jlong value, jboolean is_signed
) {
  auto* t = as_pointer<llvm::IntegerType>(type_h);
  return as_handle(llvm::ConstantInt::get(t, value, is_signed));
}

JNIEXPORT jlong JNICALL Java_scalallvm_LLVM_00024_ConstantIntCreateWide(
  JNIEnv* e, jobject, jlong context_h, jint bit_width, jlongArray words, jboolean is_signed
) {
  auto* c = as_pointer<llvm::LLVMContext>(context_h);
  auto v = arbitrary_precision_integer(e, bit_width, words, is_signed);
  return as_handle(llvm::ConstantInt::get(*c, v));
}

JNIEXPORT jlong JNICALL Java_scalallvm_LLVM_00024_ConstantIntGetSExtValue(
  JNIEnv*, jobject, jlong sh
) {
  auto* self = as_pointer<llvm::ConstantInt>(sh);
  return self->getSExtValue();
}

JNIEXPORT jlong JNICALL Java_scalallvm_LLVM_00024_ConstantIntGetZExtValue(
  JNIEnv*, jobject, jlong sh
) {
  auto* self = as_pointer<llvm::ConstantInt>(sh);
  return self->getZExtValue();
}
