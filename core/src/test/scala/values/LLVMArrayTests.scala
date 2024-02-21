package values

import scalallvm.Context
import scalallvm.types.ArrayType

class LLVMArrayTests extends munit.FunSuite {

  test("members") {
    Context.withNew { (llvm) =>
      val t0 = new ArrayType(2, llvm.i64)(llvm)
      val v0 = t0(List(llvm.i64(4), llvm.i64(2)))
      val ms = v0.members
      assertEquals(ms.length, 2)
      assertEquals(ms(0), llvm.i64(4))
      assertEquals(ms(1), llvm.i64(2))
    }
  }

}
