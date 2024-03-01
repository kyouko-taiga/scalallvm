package values

import scalallvm.{Context, InstructionBuilder, Module}
import scalallvm.types.FunctionType

class StoreTests extends munit.FunSuite {

  test("isVolatile") {
    withBuilder { (llvm, b) =>
      val s0 = b.alloca(llvm.i64)
      val s1 = b.store(llvm.i64(42), s0, isVolatile=true)
      assert(s1.isVolatile)
    }
  }

  test("alignment") {
    withBuilder { (llvm, b) =>
      val s0 = b.alloca(llvm.i64)
      val s1 = b.store(llvm.i64(42), s0, isVolatile=true)
      s1.alignment = 16
      assertEquals(s1.alignment, 16)
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
