import scalallvm.{Context, Module}
import scalallvm.types.FunctionType

class BuilderTests extends munit.FunSuite {

  test("build") {
    Module.withNew("m") { (llvm, m) =>
      val f = m.declareFunction("main", new FunctionType(Nil, llvm.i32))
      val e = f.appendBasicBlock("entry")

      llvm.withNewBuilder { (b) =>
        b.positionAtEndOf(e)
        val s00 = b.alloca(llvm.i64, size=Some(llvm.i64(2)), name="foo")
        assertEquals(s00.description.strip(), "%foo = alloca i64, i64 2, align 8")
      }
    }
  }

}
