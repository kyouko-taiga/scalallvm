import scalallvm.{Context, Module}

class ModuleTests extends munit.FunSuite {

  test("getName") {
    withNewModule("koala") { (m) => assertEquals(m.name, "koala") }
  }

  test("setName") {
    withNewModule("koala") { (m) =>
      m.name = "camel"
      assertEquals(m.name, "camel")
    }
  }

  private def withNewModule[R](n: String = "test")(action: Module => R): R =
    Context.withNew { (llvm) =>
      llvm.withNewModule(n)(action)
    }

}
