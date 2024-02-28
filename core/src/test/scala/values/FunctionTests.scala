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

  test("no entry") {
    Module.withNew("koala") { (llvm, m) =>
      val t = new FunctionType(List(llvm.i64, llvm.i32), llvm.i32)
      val f = m.declareFunction("foo", t)
      assertEquals(f.entry, None)
    }
  }

  test("basicBlocks") {
    Module.withNew("koala") { (llvm, m) =>
      val t = new FunctionType(List(llvm.i64, llvm.i32), llvm.i32)
      val f = m.declareFunction("foo", t)
      val b = f.appendBasicBlock("abc")
      assertEquals(f.basicBlocks.length, 1)
      assertEquals(f.entry, Some(b))
    }
  }

  test("appendBasicBlock") {
    Module.withNew("koala") { (llvm, m) =>
      val t = new FunctionType(List(llvm.i64, llvm.i32), llvm.i32)
      val f = m.declareFunction("foo", t)
      f.appendBasicBlock("abc")
      f.appendBasicBlock("def")

      val bs = f.basicBlocks
      assertEquals(bs(0).name, "abc")
      assertEquals(bs(1).name, "def")
    }
  }

  test("insertBasicBlock") {
    Module.withNew("koala") { (llvm, m) =>
      val t = new FunctionType(List(llvm.i64, llvm.i32), llvm.i32)
      val f = m.declareFunction("foo", t)
      val b = f.appendBasicBlock("abc")
      f.insertBasicBlock("def", b)

      val bs = f.basicBlocks
      assertEquals(bs(0).name, "def")
      assertEquals(bs(1).name, "abc")
    }
  }

}
