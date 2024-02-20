package values

import scalallvm.Context
import scalallvm.support.{FixedWidthInteger, WideInteger}
import scalallvm.values.Integer

class IntegerTests extends munit.FunSuite {

  test("init with WideInteger") {
    Context.withNew { (llvm) =>
      val i = new Integer(WideInteger.converting(-8, 8, true).get)(llvm)
      assert(i.tpe == llvm.i8)
      assertEquals(i.sext, -8L)
    }
  }

}
