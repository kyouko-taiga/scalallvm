import scalallvm.{Context, DataLayout, TargetMachine}
import scalallvm.types.StructType

class DataLayoutTests extends munit.FunSuite {

  test("bitWidth") {
    withDataLayout { (llvm, layout) =>
      assertEquals(layout.bitWidth(llvm.i32), 32)
    }
  }

  test("storageSize") {
    withDataLayout { (llvm, layout) =>
      assertEquals(layout.storageSize(llvm.i32), 4)
    }
  }

  test("preferredAlignment") {
    withDataLayout { (llvm, layout) =>
      assertEquals(layout.preferredAlignment(llvm.i32), 4)
    }
  }

  test("requiredAlignment") {
    withDataLayout { (llvm, layout) =>
      assertEquals(layout.requiredAlignment(llvm.i32), 4)
    }
  }

  test("byteOffset") {
    withDataLayout { (llvm, layout) =>
      val s = StructType.structural(List(llvm.i1, llvm.i1, llvm.i32), true)(llvm)
      assertEquals(layout.byteOffset(2, s), 2)
    }
  }

  test("indexOfElementContaining") {
    withDataLayout { (llvm, layout) =>
      val s = StructType.structural(List(llvm.i1, llvm.i1, llvm.i32), true)(llvm)
      assertEquals(layout.indexOfElementContaining(3, s), 2)
    }
  }

  test("hasPadding") {
    withDataLayout { (llvm, layout) =>
      val s = StructType.structural(List(llvm.i1, llvm.i1, llvm.i32))(llvm)
      assert(layout.hasPadding(s))
    }
  }

  def withDataLayout[R](action: (Context, DataLayout) => R): R =
    Context.withNew { (llvm) =>
      TargetMachine.withNew() { (tm) =>
        tm.withDataLayout { (layout) => action(llvm, layout) }
      }
    }

}
