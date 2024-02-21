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

template<typename T, typename R>
auto with_pointers(JNIEnv* e, jlongArray handles, const auto& action) {
  jlong* h = e->GetLongArrayElements(handles, nullptr);
  auto c = e->GetArrayLength(handles);
  T* a[c];
  for (auto i = 0; i < c; ++i) {
    a[i] = as_pointer<T>(h[i]);
  }
  e->ReleaseLongArrayElements(handles, h, 0);
  return action(llvm::ArrayRef(a, c));
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

JNIEXPORT jbyte JNICALL Java_scalallvm_LLVM_00024_TypeGetKind(
  JNIEnv*, jobject, jlong sh
) {
  auto* self = as_pointer<llvm::Type>(sh);
  return self->getTypeID();
}

JNIEXPORT jlong JNICALL Java_scalallvm_LLVM_00024_ArrayTypeCreateInContext(
  JNIEnv*, jobject, jint count, jlong element_h, jlong context_h
) {
  auto* context = as_pointer<llvm::LLVMContext>(context_h);
  auto* element = as_pointer<llvm::Type>(element_h);
  return as_handle(llvm::ArrayType::get(element, count));
}

JNIEXPORT jint JNICALL Java_scalallvm_LLVM_00024_ArrayTypeCount(
  JNIEnv*, jobject, jlong sh
) {
  auto* self = as_pointer<llvm::ArrayType>(sh);
  return self->getNumElements();
}

JNIEXPORT jlong JNICALL Java_scalallvm_LLVM_00024_ArrayTypeElement(
  JNIEnv*, jobject, jlong sh
) {
  auto* self = as_pointer<llvm::ArrayType>(sh);
  return as_handle(self->getElementType());
}

JNIEXPORT jbyte JNICALL Java_scalallvm_LLVM_00024_ArrayTypeKind(
  JNIEnv*, jobject
) {
  return llvm::Type::TypeID::ArrayTyID;
}

JNIEXPORT jlong JNICALL Java_scalallvm_LLVM_00024_DoubleTypeInContext(
  JNIEnv*, jobject, jlong context_h
) {
  auto* context = as_pointer<llvm::LLVMContext>(context_h);
  return as_handle(llvm::Type::getDoubleTy(*context));
}

JNIEXPORT jbyte JNICALL Java_scalallvm_LLVM_00024_DoubleTypeKind(
  JNIEnv*, jobject
) {
  return llvm::Type::TypeID::DoubleTyID;
}

JNIEXPORT jlong JNICALL Java_scalallvm_LLVM_00024_FloatTypeInContext(
  JNIEnv*, jobject, jlong context_h
) {
  auto* context = as_pointer<llvm::LLVMContext>(context_h);
  return as_handle(llvm::Type::getFloatTy(*context));
}

JNIEXPORT jbyte JNICALL Java_scalallvm_LLVM_00024_FloatTypeKind(
  JNIEnv*, jobject
) {
 return llvm::Type::TypeID::FloatTyID;
}

JNIEXPORT jlong JNICALL Java_scalallvm_LLVM_00024_FunctionType(
  JNIEnv* e, jobject, jlongArray parameters_h, jlong return_type_h
) {
  auto* r = as_pointer<llvm::Type>(return_type_h);
  auto* t = with_pointers<llvm::Type, llvm::FunctionType*>(e, parameters_h, [=](auto ps) {
    return llvm::FunctionType::get(r, ps, false);
  });
  return as_handle(t);
}

JNIEXPORT jbyte JNICALL Java_scalallvm_LLVM_00024_FunctionTypeKind(
  JNIEnv*, jobject
) {
  return llvm::Type::TypeID::FunctionTyID;
}

JNIEXPORT jlong JNICALL Java_scalallvm_LLVM_00024_FunctionTypeParameterAt(
  JNIEnv*, jobject, jlong sh, jint p
) {
  auto* self = as_pointer<llvm::FunctionType>(sh);
  return as_handle(self->params()[p]);
}

JNIEXPORT jint JNICALL Java_scalallvm_LLVM_00024_FunctionTypeParameterCount(
  JNIEnv*, jobject, jlong sh
) {
  auto* self = as_pointer<llvm::FunctionType>(sh);
  return self->getNumParams();
}

JNIEXPORT jlong JNICALL Java_scalallvm_LLVM_00024_FunctionTypeReturn(
  JNIEnv*, jobject, jlong sh
) {
  auto* self = as_pointer<llvm::FunctionType>(sh);
  return as_handle(self->getReturnType());
}

JNIEXPORT jlong JNICALL Java_scalallvm_LLVM_00024_IntTypeInContext(
  JNIEnv*, jobject, jint bit_width, jlong context_h
) {
  auto* c = as_pointer<llvm::LLVMContext>(context_h);
  return as_handle(llvm::IntegerType::get(*c, bit_width));
}

JNIEXPORT jbyte JNICALL Java_scalallvm_LLVM_00024_IntegerTypeKind(
  JNIEnv*, jobject
) {
  return llvm::Type::TypeID::IntegerTyID;
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

JNIEXPORT jbyte JNICALL Java_scalallvm_LLVM_00024_PointerTypeKind(
  JNIEnv*, jobject
) {
  return llvm::Type::TypeID::PointerTyID;
}

JNIEXPORT jlong JNICALL Java_scalallvm_LLVM_00024_StructTypeCreateNominalInContext(
  JNIEnv* e, jobject, jstring name, jlongArray members, jboolean is_packed, jlong context_h
) {
  auto* context = as_pointer<llvm::LLVMContext>(context_h);
  const char* n = e->GetStringUTFChars(name, NULL);
  auto* t = llvm::StructType::create(*context, n);
  with_pointers<llvm::Type, void>(e, members, [=](auto ms) {
    t->setBody(ms, is_packed);
  });
  return as_handle(t);
}

JNIEXPORT jlong JNICALL Java_scalallvm_LLVM_00024_StructTypeCreateStructuralInContext(
  JNIEnv* e, jobject, jlongArray members, jboolean is_packed, jlong context_h
) {
  auto* context = as_pointer<llvm::LLVMContext>(context_h);
  auto* t = with_pointers<llvm::Type, llvm::StructType*>(e, members, [=](auto ms) {
    return llvm::StructType::get(*context, ms, is_packed);
  });
  return as_handle(t);
}

JNIEXPORT jstring JNICALL Java_scalallvm_LLVM_00024_StructTypeGetName(
  JNIEnv* e, jobject, jlong sh
) {
  auto* self = as_pointer<llvm::StructType>(sh);
  if (self->hasName()) {
    return e->NewStringUTF(self->getName().str().c_str());
  } else {
    return nullptr;
  }
}

JNIEXPORT jboolean JNICALL Java_scalallvm_LLVM_00024_StructTypeIsLiteral(
  JNIEnv*, jobject, jlong sh
) {
  auto* self = as_pointer<llvm::StructType>(sh);
  return self->isLiteral();
}

JNIEXPORT jboolean JNICALL Java_scalallvm_LLVM_00024_StructTypeIsOpaque(
  JNIEnv*, jobject, jlong sh
) {
  auto* self = as_pointer<llvm::StructType>(sh);
  return self->isOpaque();
}

JNIEXPORT jboolean JNICALL Java_scalallvm_LLVM_00024_StructTypeIsPacked(
  JNIEnv*, jobject, jlong sh
) {
  auto* self = as_pointer<llvm::StructType>(sh);
  return self->isPacked();
}

JNIEXPORT jbyte JNICALL Java_scalallvm_LLVM_00024_StructTypeKind(
  JNIEnv*, jobject
) {
  return llvm::Type::TypeID::StructTyID;
}

JNIEXPORT jlong JNICALL Java_scalallvm_LLVM_00024_StructTypeMemberAt(
  JNIEnv *, jobject, jlong sh, jint p
) {
  auto* self = as_pointer<llvm::StructType>(sh);
  return as_handle(self->elements()[p]);
}

JNIEXPORT jint JNICALL Java_scalallvm_LLVM_00024_StructTypeMemberCount(
  JNIEnv*, jobject, jlong sh
) {
  auto* self = as_pointer<llvm::StructType>(sh);
  return self->elements().size();
}

JNIEXPORT jlong JNICALL Java_scalallvm_LLVM_00024_VoidTypeInContext(
  JNIEnv*, jobject, jlong context_h
) {
  auto* c = as_pointer<llvm::LLVMContext>(context_h);
  return as_handle(llvm::Type::getVoidTy(*c));
}

JNIEXPORT jbyte JNICALL Java_scalallvm_LLVM_00024_VoidTypeKind(
  JNIEnv*, jobject
) {
  return llvm::Type::TypeID::VoidTyID;
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

JNIEXPORT jlong JNICALL Java_scalallvm_LLVM_00024_ConstantAggregateMemberAt(
  JNIEnv*, jobject, jlong sh, jint p
) {
  auto* self = as_pointer<llvm::ConstantAggregate>(sh);
  return as_handle(self->getAggregateElement(p));
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

JNIEXPORT jlong JNICALL Java_scalallvm_LLVM_00024_ConstantDouble(
  JNIEnv*, jobject, jlong type_h, jdouble value
) {
  auto* t = as_pointer<llvm::Type>(type_h);
  return as_handle(llvm::ConstantFP::get(t, value));
}

JNIEXPORT jlong JNICALL Java_scalallvm_LLVM_00024_ConstantPoison(
  JNIEnv *, jobject, jlong type_h
) {
  auto* t = as_pointer<llvm::Type>(type_h);
  return as_handle(llvm::PoisonValue::get(t));
}

JNIEXPORT jlong JNICALL Java_scalallvm_LLVM_00024_ConstantStruct(
  JNIEnv* e, jobject, jlong type_h, jlongArray members_h
) {
  auto* s = as_pointer<llvm::StructType>(type_h);
  auto* t = with_pointers<llvm::Constant, llvm::ConstantStruct*>(e, members_h, [=](auto ms) {
    return llvm::ConstantStruct::get(s, ms);
  });
  return as_handle(t);
}

JNIEXPORT jlong JNICALL Java_scalallvm_LLVM_00024_ConstantUndefined(
  JNIEnv*, jobject, jlong type_h
) {
  auto* t = as_pointer<llvm::Type>(type_h);
  return as_handle(llvm::UndefValue::get(t));
}
