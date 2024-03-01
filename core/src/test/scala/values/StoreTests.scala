package values

import util.TestUtil

class StoreTests extends munit.FunSuite {

  test("isVolatile") {
    TestUtil.withBuilder { (llvm, b) =>
      val s0 = b.alloca(llvm.i64)
      val s1 = b.store(llvm.i64(42), s0, isVolatile=true)
      assert(s1.isVolatile)
    }
  }

  test("alignment") {
    TestUtil.withBuilder { (llvm, b) =>
      val s0 = b.alloca(llvm.i64)
      val s1 = b.store(llvm.i64(42), s0, isVolatile=true)
      s1.alignment = 16
      assertEquals(s1.alignment, 16)
    }
  }

}
