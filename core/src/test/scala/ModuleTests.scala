import scalallvm.{Context, Module}

class ModuleTests extends munit.FunSuite {

  test("getName") {
    Module.withNew("koala") { (_, m) => assertEquals(m.name, "koala") }
  }

  test("setName") {
    Module.withNew("koala") { (_, m) =>
      m.name = "camel"
      assertEquals(m.name, "camel")
    }
  }

}
