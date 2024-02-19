package types

import scalallvm.Context
import scalallvm.types.Void

class TypeTests extends munit.FunSuite {

  test("init equals") {
    Context.withNew { (llvm) =>
      val t = new Void(llvm)
      assert(t == llvm.void)
      assert(t != llvm.i8)
    }
  }

}
