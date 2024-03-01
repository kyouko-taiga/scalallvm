import scalallvm.{Context, Module}
import scalallvm.types.{FunctionType, StructType}
import scalallvm.values.Instruction

class BuilderTests extends munit.FunSuite {

  test("insertionBlock") {
    Module.withNew("m") { (llvm, m) =>
      val f = m.declareFunction("main", new FunctionType(Nil, llvm.i32))
      val e = f.appendBasicBlock("entry")

      llvm.withNewBuilder { (b) =>
        assertEquals(b.insertionBlock, None)
        b.positionAtEndOf(e)
        assertEquals(b.insertionBlock, Some(e))
      }
    }
  }

  test("getelementptr") {
    Module.withNew("m") { (llvm, m) =>
      val s = StructType.nominal("pair", List(llvm.i32, llvm.i32))(llvm)
      val f = m.declareFunction("main", new FunctionType(Nil, llvm.i32))
      val e = f.appendBasicBlock("entry")

      llvm.withNewBuilder { (b) =>
        b.positionAtEndOf(e)

        // %0 = alloca %pair, align 8
        val s00 = b.alloca(s)

        val s01 = b.structelementptr(s00, s, 1)
        check(s01, "%1 = getelementptr inbounds %pair, ptr %0, i32 0, i32 1")
        val s02 = b.getelementptr(s00, s, List(llvm.i32(0), llvm.i32(1)), inBounds=true)
        check(s02, "%2 = getelementptr inbounds %pair, ptr %0, i32 0, i32 1")
      }
    }
  }

  test("build") {
    Module.withNew("m") { (llvm, m) =>
      val f = m.declareFunction("main", new FunctionType(Nil, llvm.i32))
      val e = f.appendBasicBlock("entry")

      llvm.withNewBuilder { (b) =>
        b.positionAtEndOf(e)

        val s00 = b.alloca(llvm.i64, size=Some(llvm.i64(2)), name="s00")
        check(s00, "%s00 = alloca i64, i64 2, align 8")
        val s01 = b.getelementptr(s00, llvm.i64, List(llvm.i32(0)), name="s01")
        check(s01, "%s01 = getelementptr i64, ptr %s00, i32 0")
        val s02 = b.store(llvm.i64(42), s01)
        check(s02, "store i64 42, ptr %s01, align 4")
        val s03 = b.load(s01, llvm.i64, name="s03")
        check(s03, "%s03 = load i64, ptr %s01, align 4")
        val s04 = b.truncTo(s03, llvm.i32, name="s04")
        check(s04, "%s04 = trunc i64 %s03 to i32")
        val s05 = b.rturn(s04)
        check(s05, "ret i32 %s04")
      }

      assert(f.errors() == None)
    }
  }

  private def check(s: Instruction, description: String): Unit =
    assertEquals(s.description.strip(), description)

}
