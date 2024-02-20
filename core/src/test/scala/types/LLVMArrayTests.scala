package types

import scalallvm.Context
import scalallvm.types

class LLVMArrayTests extends munit.FunSuite {

  test("length") {
    Context.withNew { (llvm) =>
      val t0 = new types.LLVMArray(8, llvm.i8)(llvm)
      assertEquals(t0.length, 8)
    }
  }

  test("element") {
    Context.withNew { (llvm) =>
      val t0 = new types.LLVMArray(8, llvm.i8)(llvm)
      assertEquals(t0.element, llvm.i8)
    }
  }

}
