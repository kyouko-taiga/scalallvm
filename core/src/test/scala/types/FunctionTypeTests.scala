package types

import scalallvm.Context
import scalallvm.types

class FunctionTypeTests extends munit.FunSuite {

  test("parameters") {
    Context.withNew { (llvm) =>
      val t0 = new types.FunctionType(List(llvm.i64, llvm.i32), llvm.i1)
      val ps = t0.parameters
      assertEquals(ps.length, 2)
      assertEquals(ps(0), llvm.i64)
      assertEquals(ps(1), llvm.i32)
    }
  }

  test("returnType") {
    Context.withNew { (llvm) =>
      val t0 = new types.FunctionType(List(llvm.i64, llvm.i32), llvm.i1)
      assertEquals(t0.returnType, llvm.i1)
    }
  }

}
