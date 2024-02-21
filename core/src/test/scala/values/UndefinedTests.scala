package values

import scalallvm.Context
import scalallvm.values.Undefined

class UndefinedTests extends munit.FunSuite {

  test("equals") {
    Context.withNew { (llvm) =>
      val u = new Undefined(llvm.i64)
      assert(u != llvm.i1(1))
    }
  }

}
