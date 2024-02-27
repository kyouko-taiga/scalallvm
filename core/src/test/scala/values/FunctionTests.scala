package values

import scalallvm.{Module}
import scalallvm.types.FunctionType

class FunctionTests extends munit.FunSuite {

  test("getName") {
    Module.withNew("koala") { (llvm, m) =>
      val t = new FunctionType(List(llvm.i32), llvm.i32)
      val f = m.declareFunction("foo", t)
      assertEquals(f.name, "foo")
    }
  }

  test("parameters") {
    Module.withNew("koala") { (llvm, m) =>
      val t = new FunctionType(List(llvm.i64, llvm.i32), llvm.i32)
      val f = m.declareFunction("foo", t)
      val ps = f.parameters
      assertEquals(ps.length, 2)
      assertEquals(ps(0).tpe, llvm.i64)
      assertEquals(ps(1).tpe, llvm.i32)
    }
  }

}
