package values

import scalallvm.{AddressSpace, Context, InstructionBuilder, Module}
import scalallvm.values.Alloca
import scalallvm.types.FunctionType

class AllocaTests extends munit.FunSuite {

  test("isStatic") {
    withBuilder { (llvm, b) =>
      val s = b.alloca(llvm.i64, size=Some(llvm.i64(2)))
      assert(s.isStatic)
    }
  }

  test("alignment") {
    withBuilder { (llvm, b) =>
      val s = b.alloca(llvm.i64, size=Some(llvm.i64(2)))
      assertEquals(s.alignment, 8)
      s.alignment = 16
      assertEquals(s.alignment, 16)
    }
  }

  test("allocatedType") {
    withBuilder { (llvm, b) =>
      val s = b.alloca(llvm.i64, size=Some(llvm.i64(2)))
      assertEquals(s.allocatedType, llvm.i64)
    }
  }

  test("addressSpace") {
    withBuilder { (llvm, b) =>
      val s = b.alloca(llvm.i64, size=Some(llvm.i64(2)))
      assertEquals(s.addressSpace, AddressSpace.default)
    }
  }

  test("allocationCount") {
    withBuilder { (llvm, b) =>
      val s = b.alloca(llvm.i64, size=Some(llvm.i64(2)))
      assertEquals(s.allocationCount, llvm.i64(2))
    }
  }

  private def withBuilder[R](action: (Context, InstructionBuilder) => R): R = {
    Module.withNew("m") { (llvm, m) =>
      val f = m.declareFunction("main", new FunctionType(Nil, llvm.i32))
      val e = f.appendBasicBlock("entry")

      llvm.withNewBuilder { (b) =>
        b.positionAtEndOf(e)
        action(llvm, b)
      }
    }
  }

}
