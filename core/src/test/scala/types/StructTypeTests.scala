package types

import scalallvm.Context
import scalallvm.types

class StructTypeTests extends munit.FunSuite {

  test("init nominal") {
    Context.withNew { (llvm) =>
      val t0 = types.StructType.nominal("abc", List(llvm.i64, llvm.i64), false)(llvm)
      assert(!t0.isLiteral)
      assertEquals(t0.name, Some("abc"))

      val t1 = types.StructType.nominal("abc", List(llvm.i64, llvm.i64), false)(llvm)
      assertNotEquals(t0, t1)
    }
  }

  test("init structural") {
    Context.withNew { (llvm) =>
      val t0 = types.StructType.structural(List(llvm.i64, llvm.i64), false)(llvm)
      assert(t0.isLiteral)
      assertEquals(t0.name, None)

      val t1 = types.StructType.structural(List(llvm.i64, llvm.i64), false)(llvm)
      assertEquals(t0, t1)
    }
  }

  test("packed") {
    Context.withNew { (llvm) =>
      val t0 = types.StructType.structural(List(llvm.i8, llvm.i16, llvm.i8), true)(llvm)
      assert(t0.isPacked)
    }
  }

  test("members") {
    Context.withNew { (llvm) =>
      val t0 = types.StructType.structural(List(llvm.i64, llvm.i32), false)(llvm)
      val ms = t0.members
      assertEquals(ms.length, 2)
      assertEquals(ms(0), llvm.i64)
      assertEquals(ms(1), llvm.i32)
    }
  }

}
