package values

import scalallvm.Context
import scalallvm.types.ArrayType
import scalallvm.values.LLVMArray

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

  test("string") {
    Context.withNew { (llvm) =>
      val v0 = LLVMArray.string("A", nullTerminated = true)(llvm)
      val t1 = new ArrayType(2, llvm.i8)(llvm)
      val v1 = t1(List(llvm.i8(65), llvm.i8(0)))
      assertEquals(v0.length, 2)
      assertEquals(v0, v1)

      val v2 = LLVMArray.string("A", nullTerminated = false)(llvm)
      assertEquals(v2.length, 1)
    }
  }

}
