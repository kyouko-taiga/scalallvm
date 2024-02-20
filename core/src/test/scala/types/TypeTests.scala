package types

import scalallvm.Context
import scalallvm.types

class TypeTests extends munit.FunSuite {

  test("init equals") {
    Context.withNew { (llvm) =>
      val t = new types.VoidType(llvm)
      assert(t == llvm.void)
      assert(t != llvm.i8)
    }
  }

}
