package values

import scalallvm.Context
import scalallvm.values.Poison

class PoisonTests extends munit.FunSuite {

  test("equals") {
    Context.withNew { (llvm) =>
      val u = new Poison(llvm.i64)
      assert(u != llvm.i1(1))
    }
  }

}
