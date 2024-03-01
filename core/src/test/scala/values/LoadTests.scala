package values

import util.TestUtil

class LoadTests extends munit.FunSuite {

  test("isVolatile") {
    TestUtil.withBuilder { (llvm, b) =>
      val s0 = b.alloca(llvm.i64)
      val s1 = b.store(llvm.i64(42), s0, isVolatile=true)
      val s2 = b.load(s1, llvm.i64, isVolatile=true)
      assert(s2.isVolatile)
    }
  }

  test("alignment") {
    TestUtil.withBuilder { (llvm, b) =>
      val s0 = b.alloca(llvm.i64)
      val s1 = b.store(llvm.i64(42), s0, isVolatile=true)
      val s2 = b.load(s1, llvm.i64, isVolatile=true)
      s2.alignment = 16
      assertEquals(s2.alignment, 16)
    }
  }

}
