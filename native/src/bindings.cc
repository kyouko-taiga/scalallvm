#include <concepts>

#include <llvm/ADT/APInt.h>
#include <llvm/IR/Constants.h>
#include <llvm/IR/IRBuilder.h>
#include <llvm/IR/LLVMContext.h>
#include <llvm/IR/Module.h>
#include <llvm/IR/Verifier.h>
#include <llvm/Support/raw_ostream.h>

#include "scalallvm_LLVM_00024.h"

/// Converts `p` into a Java handle.
jlong as_handle(void* p) {
  return reinterpret_cast<uintptr_t>(p);
}

/// Converts `p` into a nullable Java handle.
jlong as_nullable_handle(void* p) {
  return (p == nullptr) ? 0 : as_handle(p);
}

/// Unsafely converts the Java handle `h` to a pointer of type `T`.
template<typename T>
T* as_pointer(jlong h) {
  return static_cast<T*>(reinterpret_cast<void*>(h));
}

template<typename T>
T* as_nullable_pointer(jlong h) {
  return (h == 0) ?  nullptr : static_cast<T*>(reinterpret_cast<void*>(h));
}

/// Returns the result of calling `action` on `llvm::ArrayRef` of pointers converted as pointers of
/// `T` from the elements in `handles`.
template<typename T, typename F>
requires std::invocable<F, llvm::ArrayRef<T*> const&>
auto with_pointers(JNIEnv* e, jlongArray handles, F&& action) {
  jlong* h = e->GetLongArrayElements(handles, nullptr);
  auto c = e->GetArrayLength(handles);
  T* a[c];
  for (auto i = 0; i < c; ++i) {
    a[i] = as_pointer<T>(h[i]);
  }
  e->ReleaseLongArrayElements(handles, h, 0);
  return action(llvm::ArrayRef(a, c));
}

/// Returns the result of calling `action` on a buffer containing the utf-8 contents of `s`.
template<typename F>
requires std::invocable<F, const char*>
auto with_utf8(JNIEnv* e, jstring s, F&& action) {
  const char* n = e->GetStringUTFChars(s, NULL);
  auto result = action(n);
  e->ReleaseStringUTFChars(s, n);
  return result;
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

/// Returns an instance of `llvm::GlobalValue::LinkageTypes` from its raw value.
llvm::GlobalValue::LinkageTypes linkage(jbyte raw_value) {
  return static_cast<llvm::GlobalValue::LinkageTypes>(raw_value);
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

JNIEXPORT jlong JNICALL Java_scalallvm_LLVM_00024_InstructionBuilderGetInsertionBlock(
  JNIEnv*, jobject, jlong sh
) {
  auto* self = as_pointer<llvm::IRBuilderBase>(sh);
  return as_nullable_handle(self->GetInsertBlock());
}

JNIEXPORT jlong JNICALL Java_scalallvm_LLVM_00024_InstructionBuilderMakeAlloca(
  JNIEnv* e, jobject, jlong sh, jlong type_h, jint space, jlong size_h, jstring name
) {
  auto* self = as_pointer<llvm::IRBuilderBase>(sh);
  auto* type = as_pointer<llvm::Type>(type_h);
  auto* size = as_nullable_pointer<llvm::Value>(size_h);
  return with_utf8(e, name, [=](auto n) {
    return as_handle(self->CreateAlloca(type, space, size, n));
  });
}

JNIEXPORT jlong JNICALL Java_scalallvm_LLVM_00024_InstructionBuilderMakeBr(
  JNIEnv*, jobject, jlong sh, jlong destination_h
) {
  auto* self = as_pointer<llvm::IRBuilderBase>(sh);
  auto* destination = as_pointer<llvm::BasicBlock>(destination_h);
  return as_handle(self->CreateBr(destination));
}

JNIEXPORT jlong JNICALL Java_scalallvm_LLVM_00024_InstructionBuilderMakeConditionalBr(
  JNIEnv*, jobject, jlong sh, jlong condition_h, jlong success_h, jlong failure_h
) {
  auto* self = as_pointer<llvm::IRBuilderBase>(sh);
  auto* c = as_pointer<llvm::Value>(condition_h);
  auto* s = as_pointer<llvm::BasicBlock>(success_h);
  auto* f = as_pointer<llvm::BasicBlock>(failure_h);
  return as_handle(self->CreateCondBr(c, s, f));
}

JNIEXPORT jlong JNICALL Java_scalallvm_LLVM_00024_InstructionBuilderMakeGEP(
  JNIEnv* e, jobject, jlong sh, jlong base_h, jlong base_type_h, jlongArray indices_h,
  jstring name, jboolean inBounds
) {
  auto* self = as_pointer<llvm::IRBuilderBase>(sh);
  auto* b = as_pointer<llvm::Value>(base_h);
  auto* t = as_pointer<llvm::Type>(base_type_h);
  return with_pointers<llvm::Value>(e, indices_h, [=](auto is) {
    return with_utf8(e, name, [=](auto n) {
      return as_handle(self->CreateGEP(t, b, is, n, inBounds));
    });
  });
}

JNIEXPORT jlong JNICALL Java_scalallvm_LLVM_00024_InstructionBuilderMakeLoad(
  JNIEnv* e, jobject, jlong sh, jlong source_h, jlong source_type_h,
  jboolean is_volatile, jstring name
) {
  auto* self = as_pointer<llvm::IRBuilderBase>(sh);
  auto* s = as_pointer<llvm::Value>(source_h);
  auto* t = as_pointer<llvm::Type>(source_type_h);
  return with_utf8(e, name, [=](auto n) {
    return as_handle(self->CreateLoad(t, s, is_volatile, n));
  });
}

JNIEXPORT jlong JNICALL Java_scalallvm_LLVM_00024_InstructionBuilderMakeReturn(
  JNIEnv*, jobject, jlong sh, jlong value_h
) {
  auto* self = as_pointer<llvm::IRBuilderBase>(sh);
  auto* v = as_nullable_pointer<llvm::Value>(value_h);
  if (v == nullptr) {
    return as_handle(self->CreateRetVoid());
  } else {
    return as_handle(self->CreateRet(v));
  }
}

JNIEXPORT jlong JNICALL Java_scalallvm_LLVM_00024_InstructionBuilderMakeStore(
  JNIEnv* e, jobject, jlong sh, jlong value_h, jlong target_h, jboolean is_volatile
) {
  auto* self = as_pointer<llvm::IRBuilderBase>(sh);
  auto* v = as_nullable_pointer<llvm::Value>(value_h);
  auto* t = as_nullable_pointer<llvm::Value>(target_h);
  return as_handle(self->CreateStore(v, t, is_volatile));
}

JNIEXPORT jlong JNICALL Java_scalallvm_LLVM_00024_InstructionBuilderMakeStructGEP(
  JNIEnv* e, jobject, jlong sh, jlong base_h, jlong base_type_h, jint index, jstring name
) {
  auto* self = as_pointer<llvm::IRBuilderBase>(sh);
  auto* b = as_pointer<llvm::Value>(base_h);
  auto* t = as_pointer<llvm::Type>(base_type_h);
  return with_utf8(e, name, [=](auto n) {
    return as_handle(self->CreateStructGEP(t, b, index, n));
  });
}

JNIEXPORT jlong JNICALL Java_scalallvm_LLVM_00024_InstructionBuilderMakeSwitch(
  JNIEnv* e, jobject, jlong sh, jlong condition_h, jlong default_h,
  jlongArray patterns_h, jlongArray destinations_h
) {
  auto* self = as_pointer<llvm::IRBuilderBase>(sh);
  auto* c = as_pointer<llvm::Value>(condition_h);
  auto* d = as_pointer<llvm::BasicBlock>(default_h);
  return with_pointers<llvm::ConstantInt>(e, patterns_h, [=](auto ps) {
    return with_pointers<llvm::BasicBlock>(e, destinations_h, [=](auto ds) {
      auto* s = self->CreateSwitch(c, d, ps.size());
      for (int i = 0; i < ps.size(); ++i) {
        s->addCase(ps[i], ds[i]);
      }
      return as_handle(s);
    });
  });
}

JNIEXPORT jlong JNICALL Java_scalallvm_LLVM_00024_InstructionBuilderMakeTruncTo(
  JNIEnv* e, jobject, jlong sh, jlong source_h, jlong target_h, jstring name
) {
  auto* self = as_pointer<llvm::IRBuilderBase>(sh);
  auto* s = as_pointer<llvm::Value>(source_h);
  auto* t = as_pointer<llvm::Type>(target_h);
  return with_utf8(e, name, [=](auto n) {
    return as_handle(self->CreateTrunc(s, t, n));
  });
}

JNIEXPORT jlong JNICALL Java_scalallvm_LLVM_00024_InstructionBuilderMakeUnreachable(
  JNIEnv*, jobject, jlong sh
) {
  auto* self = as_pointer<llvm::IRBuilderBase>(sh);
  return as_handle(self->CreateUnreachable());
}

JNIEXPORT jlong JNICALL Java_scalallvm_LLVM_00024_ModuleCreateWithNameInContext(
  JNIEnv* e, jobject, jstring name, jlong context_h
) {
  auto* context = as_pointer<llvm::LLVMContext>(context_h);
  auto* result = with_utf8(e, name, [=](auto n) { return new llvm::Module(n, *context); });
  return as_handle(result);
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
  with_utf8(e, name, [=](auto n) {
    self->setModuleIdentifier(n);
    return std::monostate{};
  });
}

JNIEXPORT jlong JNICALL Java_scalallvm_LLVM_00024_InstructionBuilderCreateInContext(
  JNIEnv*, jobject, jlong context_h
) {
  auto* context = as_pointer<llvm::LLVMContext>(context_h);
  return as_handle(new llvm::IRBuilder(*context));
}

JNIEXPORT void JNICALL Java_scalallvm_LLVM_00024_InstructionBuilderDispose(
  JNIEnv*, jobject, jlong sh
) {
  delete as_pointer<llvm::IRBuilderBase>(sh);
}

JNIEXPORT void JNICALL Java_scalallvm_LLVM_00024_InstructionBuilderPositionAtEndOfBlock(
  JNIEnv *, jobject, jlong sh, jlong block_h
) {
  auto* self = as_pointer<llvm::IRBuilderBase>(sh);
  auto* block = as_pointer<llvm::BasicBlock>(block_h);
  self->SetInsertPoint(block);
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
  auto* t = with_pointers<llvm::Type>(e, parameters_h, [=](auto ps) {
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
  auto* result = with_utf8(e, name, [=](auto n) { return llvm::StructType::create(*context, n); });
  with_pointers<llvm::Type>(e, members, [=](auto ms) {
    result->setBody(ms, is_packed);
  });
  return as_handle(result);
}

JNIEXPORT jlong JNICALL Java_scalallvm_LLVM_00024_StructTypeCreateStructuralInContext(
  JNIEnv* e, jobject, jlongArray members, jboolean is_packed, jlong context_h
) {
  auto* context = as_pointer<llvm::LLVMContext>(context_h);
  auto* t = with_pointers<llvm::Type>(e, members, [=](auto ms) {
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

JNIEXPORT jstring JNICALL Java_scalallvm_LLVM_00024_ValueGetName(
  JNIEnv* e, jobject, jlong sh
) {
  auto* self = as_pointer<llvm::Value>(sh);
  return e->NewStringUTF(self->getName().str().c_str());
}

JNIEXPORT jlong JNICALL Java_scalallvm_LLVM_00024_ValueGetType(
  JNIEnv*, jobject, jlong sh
) {
  auto* self = as_pointer<llvm::Value>(sh);
  return as_handle(self->getType());
}

JNIEXPORT jint JNICALL Java_scalallvm_LLVM_00024_AllocaGetAddressSpace(
  JNIEnv*, jobject, jlong sh
) {
  auto* self = as_pointer<llvm::AllocaInst>(sh);
  return self->getAddressSpace();
}

JNIEXPORT jlong JNICALL Java_scalallvm_LLVM_00024_AllocaGetAlignment(
  JNIEnv*, jobject, jlong sh
) {
  auto* self = as_pointer<llvm::AllocaInst>(sh);
  return self->getAlign().value();
}

JNIEXPORT jlong JNICALL Java_scalallvm_LLVM_00024_AllocaGetAllocatedType(
  JNIEnv*, jobject, jlong sh
) {
  auto* self = as_pointer<llvm::AllocaInst>(sh);
  return as_handle(self->getAllocatedType());
}

JNIEXPORT jlong JNICALL Java_scalallvm_LLVM_00024_AllocaGetAllocationCount(
  JNIEnv*, jobject, jlong sh
) {
  auto* self = as_pointer<llvm::AllocaInst>(sh);
  return as_handle(self->getArraySize());
}

JNIEXPORT jboolean JNICALL Java_scalallvm_LLVM_00024_AllocaIsStatic(
  JNIEnv*, jobject, jlong sh
) {
  auto* self = as_pointer<llvm::AllocaInst>(sh);
  return self->isStaticAlloca();
}

JNIEXPORT jlong JNICALL Java_scalallvm_LLVM_00024_AllocaFromValue(
  JNIEnv*, jobject, jlong source_h
) {
  auto* source = as_pointer<llvm::Value>(source_h);
  return as_nullable_handle(llvm::dyn_cast<llvm::AllocaInst>(source));
}

JNIEXPORT void JNICALL Java_scalallvm_LLVM_00024_AllocaSetAlignment(
  JNIEnv *, jobject, jlong sh, jlong a
) {
  auto* self = as_pointer<llvm::AllocaInst>(sh);
  self->setAlignment(llvm::Align(a));
}

JNIEXPORT jlong JNICALL Java_scalallvm_LLVM_00024_BasicBlockCreateInParent(
  JNIEnv* e, jobject, jstring name, jlong next, jlong parent_h
) {
  auto* parent = as_pointer<llvm::Function>(parent_h);
  auto* result = with_utf8(e, name, [=](auto n) {
    auto* b = (next != 0) ? as_pointer<llvm::BasicBlock>(next) : nullptr;
    return llvm::BasicBlock::Create(parent->getContext(), n, parent, b);
  });
  return as_handle(result);
}

JNIEXPORT jlong JNICALL Java_scalallvm_LLVM_00024_BasicBlockGetParent(
  JNIEnv* e, jobject, jlong sh
) {
  auto* self = as_pointer<llvm::BasicBlock>(sh);
  return as_handle(self->getParent());
}

JNIEXPORT jboolean JNICALL Java_scalallvm_LLVM_00024_BranchIsConditional(
  JNIEnv*, jobject, jlong sh
) {
  auto* self = as_pointer<llvm::BranchInst>(sh);
  return self->isConditional();
}

JNIEXPORT jlong JNICALL Java_scalallvm_LLVM_00024_BranchGetCondition(
  JNIEnv*, jobject, jlong sh
) {
  auto* self = as_pointer<llvm::BranchInst>(sh);
  return self->isConditional() ? as_handle(self->getCondition()) : 0;
}

JNIEXPORT jlong JNICALL Java_scalallvm_LLVM_00024_BranchFromValue(
  JNIEnv*, jobject, jlong source_h
) {
  auto* source = as_pointer<llvm::Value>(source_h);
  return as_nullable_handle(llvm::dyn_cast<llvm::BranchInst>(source));
}

JNIEXPORT jlong JNICALL Java_scalallvm_LLVM_00024_BranchSuccessorAt(
  JNIEnv*, jobject, jlong sh, jint p
) {
  auto* self = as_pointer<llvm::BranchInst>(sh);
  return as_handle(self->getSuccessor(p));
}

JNIEXPORT jlong JNICALL Java_scalallvm_LLVM_00024_ConstantAggregateMemberAt(
  JNIEnv*, jobject, jlong sh, jint p
) {
  auto* self = as_pointer<llvm::ConstantAggregate>(sh);
  return as_handle(self->getAggregateElement(p));
}

JNIEXPORT jlong JNICALL Java_scalallvm_LLVM_00024_ConstantArray(
  JNIEnv* e, jobject, jlong element_h, jlongArray members_h
) {
  auto* element = as_pointer<llvm::Type>(element_h);
  auto* result = with_pointers<llvm::Constant>(e, members_h, [=](auto ms) {
    auto* a = llvm::ArrayType::get(element, ms.size());
    return llvm::ConstantArray::get(a, ms);
  });
  return as_handle(result);
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

JNIEXPORT jlong JNICALL Java_scalallvm_LLVM_00024_ConstantStringInContext(
  JNIEnv* e, jobject, jstring text, jboolean null_terminated, jlong context_h
) {
  auto* context = as_pointer<llvm::LLVMContext>(context_h);
  auto* result = with_utf8(e, text, [=](auto n) {
    return llvm::ConstantDataArray::getString(*context, n, null_terminated);
  });
  return as_handle(result);
}

JNIEXPORT jlong JNICALL Java_scalallvm_LLVM_00024_ConstantStruct(
  JNIEnv* e, jobject, jlong type_h, jlongArray members_h
) {
  auto* s = as_pointer<llvm::StructType>(type_h);
  auto* t = with_pointers<llvm::Constant>(e, members_h, [=](auto ms) {
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

JNIEXPORT jlong JNICALL Java_scalallvm_LLVM_00024_LoadGetAlignment(
  JNIEnv*, jobject, jlong sh
) {
  auto* self = as_pointer<llvm::LoadInst>(sh);
  return self->getAlign().value();
}

JNIEXPORT jboolean JNICALL Java_scalallvm_LLVM_00024_LoadIsVolatile(
  JNIEnv*, jobject, jlong sh
) {
  auto* self = as_pointer<llvm::LoadInst>(sh);
  return self->isVolatile();
}

JNIEXPORT jlong JNICALL Java_scalallvm_LLVM_00024_LoadFromValue(
  JNIEnv*, jobject, jlong source_h
) {
  auto* source = as_pointer<llvm::Value>(source_h);
  return as_nullable_handle(llvm::dyn_cast<llvm::LoadInst>(source));
}

JNIEXPORT void JNICALL Java_scalallvm_LLVM_00024_LoadSetAlignment(
  JNIEnv *, jobject, jlong sh, jlong a
) {
  auto* self = as_pointer<llvm::LoadInst>(sh);
  self->setAlignment(llvm::Align(a));
}

JNIEXPORT jlong JNICALL Java_scalallvm_LLVM_00024_StoreGetAlignment(
  JNIEnv*, jobject, jlong sh
) {
  auto* self = as_pointer<llvm::StoreInst>(sh);
  return self->getAlign().value();
}

JNIEXPORT jboolean JNICALL Java_scalallvm_LLVM_00024_StoreIsVolatile(
  JNIEnv*, jobject, jlong sh
) {
  auto* self = as_pointer<llvm::StoreInst>(sh);
  return self->isVolatile();
}

JNIEXPORT jlong JNICALL Java_scalallvm_LLVM_00024_StoreFromValue(
  JNIEnv*, jobject, jlong source_h
) {
  auto* source = as_pointer<llvm::Value>(source_h);
  return as_nullable_handle(llvm::dyn_cast<llvm::StoreInst>(source));
}

JNIEXPORT void JNICALL Java_scalallvm_LLVM_00024_StoreSetAlignment(
  JNIEnv *, jobject, jlong sh, jlong a
) {
  auto* self = as_pointer<llvm::StoreInst>(sh);
  self->setAlignment(llvm::Align(a));
}

JNIEXPORT jlong JNICALL Java_scalallvm_LLVM_00024_SwitchGetCondition(
  JNIEnv*, jobject, jlong sh
) {
  auto* self = as_pointer<llvm::SwitchInst>(sh);
  return as_handle(self->getCondition());
}

JNIEXPORT jlong JNICALL Java_scalallvm_LLVM_00024_SwitchGetDefaultDestination(
  JNIEnv*, jobject, jlong sh
) {
  auto* self = as_pointer<llvm::SwitchInst>(sh);
  return as_handle(self->getDefaultDest());
}

JNIEXPORT jlong JNICALL Java_scalallvm_LLVM_00024_SwitchFromValue(
  JNIEnv*, jobject, jlong source_h
) {
  auto* source = as_pointer<llvm::Value>(source_h);
  return as_nullable_handle(llvm::dyn_cast<llvm::SwitchInst>(source));
}

JNIEXPORT jlong JNICALL Java_scalallvm_LLVM_00024_SwitchSuccessorAt(
  JNIEnv*, jobject, jlong sh, jint p
) {
  auto* self = as_pointer<llvm::SwitchInst>(sh);
  return as_handle(self->getSuccessor(p));
}

JNIEXPORT jint JNICALL Java_scalallvm_LLVM_00024_SwitchSuccessorCount(
  JNIEnv*, jobject, jlong sh
) {
  auto* self = as_pointer<llvm::SwitchInst>(sh);
  return self->getNumSuccessors();
}

/// Creates a function in a module using `llvm::Function::Create`.
///
/// - Parameters:
///   - name: The name of the function.
///   - type_h: A handle to a `llvm::FunctionType`.
///   - linkage: The linkage of the function.
///   - space: The address space in which the function is created.
///   - module_h: A handle to a `llvm::Module`.
JNIEXPORT jlong JNICALL Java_scalallvm_LLVM_00024_FunctionCreateInModule(
  JNIEnv* e, jobject, jstring name, jlong type_h, jbyte linkage_r, jint space, jlong module_h
) {
  auto* m = as_pointer<llvm::Module>(module_h);
  auto* t = as_pointer<llvm::FunctionType>(type_h);
  auto l = linkage(linkage_r);
  auto* result = with_utf8(e, name, [=](auto n) {
    return llvm::Function::Create(t, l, space, n, m);
  });
  return as_handle(result);
}

JNIEXPORT jlong JNICALL Java_scalallvm_LLVM_00024_FunctionBasicBlockEntry(
  JNIEnv*, jobject, jlong sh
) {
  auto* self = as_pointer<llvm::Function>(sh);
  return (self->size() > 0) ? as_handle(&(self->getEntryBlock())) : 0;
}

JNIEXPORT jlongArray JNICALL Java_scalallvm_LLVM_00024_FunctionBasicBlocks(
  JNIEnv* e, jobject, jlong sh
) {
  auto* self = as_pointer<llvm::Function>(sh);
  jlongArray result = e->NewLongArray(self->size());

  int i = 0;
  jlong handles[self->size()];
  for (auto& b : *self) { handles[i++] = as_handle(&b); }
  e->SetLongArrayRegion(result, 0, self->size(), handles);

  return result;
}

JNIEXPORT jlong JNICALL Java_scalallvm_LLVM_00024_FunctionGetByNameInModule(
  JNIEnv* e, jobject, jstring name, jlong module_h
) {
  auto* m = as_pointer<llvm::Module>(module_h);
  return with_utf8(e, name, [=](auto n) {
    return as_nullable_handle(m->getFunction(n));
  });
}

JNIEXPORT jlong JNICALL Java_scalallvm_LLVM_00024_FunctionParameterAfter(
  JNIEnv*, jobject, jlong, jlong p
) {
  auto* a = as_pointer<llvm::Argument>(p);
  return as_handle(++a);
}

JNIEXPORT jlong JNICALL Java_scalallvm_LLVM_00024_FunctionParameterBegin(
  JNIEnv*, jobject, jlong sh
) {
  auto* self = as_pointer<llvm::Function>(sh);
  return as_handle(self->arg_begin());
}

JNIEXPORT jint JNICALL Java_scalallvm_LLVM_00024_FunctionParameterCount(
  JNIEnv*, jobject, jlong sh
) {
  auto* self = as_pointer<llvm::Function>(sh);
  return self->arg_size();
}

JNIEXPORT jlong JNICALL Java_scalallvm_LLVM_00024_FunctionParameterEnd(
  JNIEnv*, jobject, jlong sh
) {
  auto* self = as_pointer<llvm::Function>(sh);
  return as_handle(self->arg_end());
}

JNIEXPORT jstring JNICALL Java_scalallvm_LLVM_00024_FunctionVerifiy(
  JNIEnv* e, jobject, jlong sh
) {
  auto* self = as_pointer<llvm::Function>(sh);
  std::string s;
  llvm::raw_string_ostream o(s);
  if (llvm::verifyFunction(*self, &o)) {
    return e->NewStringUTF(s.c_str());
  } else {
    return 0;
  }
}

JNIEXPORT jbyte JNICALL Java_scalallvm_LLVM_00024_LinkageExternal(
  JNIEnv*, jobject
) {
  return llvm::GlobalValue::LinkageTypes::ExternalLinkage;
}
