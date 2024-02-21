package values

import scalallvm.Context
import scalallvm.types.StructType

class StructTests extends munit.FunSuite {

  test("members") {
    Context.withNew { (llvm) =>
      val t0 = StructType.structural(List(llvm.i64, llvm.i32))(llvm)
      val v0 = t0(List(llvm.i64(4), llvm.i32(2)))
      val ms = v0.members
      assertEquals(ms.length, 2)
      assertEquals(ms(0), llvm.i64(4))
      assertEquals(ms(1), llvm.i32(2))
    }
  }

}
