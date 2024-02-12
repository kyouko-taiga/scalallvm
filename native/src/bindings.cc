#include <iostream>
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

JNIEXPORT void JNICALL Java_scalallvm_LLVM_00024_ModuleDispose(
  JNIEnv*, jobject, jlong sh
) {
  delete as_pointer<llvm::Module>(sh);
}
