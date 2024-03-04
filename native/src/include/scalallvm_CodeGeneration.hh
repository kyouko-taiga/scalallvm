#ifndef SCALALLVM_CODE_GENERATION_HH
#define SCALALLVM_CODE_GENERATION_HH

#include <cstdint>

#include <llvm/ADT/Optional.h>
#include <llvm/Support/CodeGen.h>
#include <llvm/Support/ErrorHandling.h>

namespace scalallvm {

/// Converts a code model from Scala LLVM to LLVM.
inline llvm::Optional<llvm::CodeModel::Model> decode_code_model(uint8_t m) {
  switch (m) {
    case 0: return llvm::None;
    case 1: return llvm::CodeModel::Tiny;
    case 2: return llvm::CodeModel::Small;
    case 3: return llvm::CodeModel::Kernel;
    case 4: return llvm::CodeModel::Medium;
    case 5: return llvm::CodeModel::Large;
    default: llvm_unreachable("unexpected code model");
  }
}

/// Converts a code generation optimization level from Scala LLVM to LLVM.
inline llvm::CodeGenOpt::Level decode_codegen_optimization_level(uint8_t m) {
  switch (m & 0b1111) {
    case 0: return llvm::CodeGenOpt::Level::None;
    case 1: return llvm::CodeGenOpt::Level::Less;
    case 2: return llvm::CodeGenOpt::Level::Default;
    case 3: return llvm::CodeGenOpt::Level::Aggressive;
    default: llvm_unreachable("unexpected codegen optimization level");
  }
}

/// Converts a relocation model from Scala LLVM to LLVM.
inline llvm::Optional<llvm::Reloc::Model> decode_relocation_model(uint8_t m) {
  switch (m) {
    case 0: return llvm::None;
    case 1: return llvm::Reloc::Static;
    case 2: return llvm::Reloc::PIC_;
    default: llvm_unreachable("unexpected relocation model");
  }
}

/// Converts

}

// SCALALLVM_CODE_GENERATION_HH
#endif
