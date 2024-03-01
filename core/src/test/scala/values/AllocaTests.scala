package values

import scalallvm.AddressSpace

import util.TestUtil

class AllocaTests extends munit.FunSuite {

  test("isStatic") {
    TestUtil.withBuilder { (llvm, b) =>
      val s = b.alloca(llvm.i64, size=Some(llvm.i64(2)))
      assert(s.isStatic)
    }
  }

  test("alignment") {
    TestUtil.withBuilder { (llvm, b) =>
      val s = b.alloca(llvm.i64, size=Some(llvm.i64(2)))
      s.alignment = 16
      assertEquals(s.alignment, 16)
    }
  }

  test("allocatedType") {
    TestUtil.withBuilder { (llvm, b) =>
      val s = b.alloca(llvm.i64, size=Some(llvm.i64(2)))
      assertEquals(s.allocatedType, llvm.i64)
    }
  }

  test("addressSpace") {
    TestUtil.withBuilder { (llvm, b) =>
      val s = b.alloca(llvm.i64, size=Some(llvm.i64(2)))
      assertEquals(s.addressSpace, AddressSpace.default)
    }
  }

  test("allocationCount") {
    TestUtil.withBuilder { (llvm, b) =>
      val s = b.alloca(llvm.i64, size=Some(llvm.i64(2)))
      assertEquals(s.allocationCount, llvm.i64(2))
    }
  }

}
