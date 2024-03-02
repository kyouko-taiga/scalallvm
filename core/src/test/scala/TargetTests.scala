import scalallvm.Target

class TargetTests extends munit.FunSuite {

  test("host") {
    assertNotEquals(Target.hostTriple, "")
  }

  test("init with invalid triple") {
    var errorWasThrown = false
    try {
      Target("does-not-exist")
    } catch {
      case _: Throwable => errorWasThrown = true
    }
    assert(errorWasThrown)
  }

}
