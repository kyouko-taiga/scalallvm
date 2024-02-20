package types

import scalallvm.{AddressSpace, Context}
import scalallvm.types

class PointerTypeTests extends munit.FunSuite {

  test("addressSpace") {
    Context.withNew { (llvm) =>
      val p0 = new types.PointerType()(llvm)
      assert(p0.addressSpace == AddressSpace.default)
      val p1 = new types.PointerType(AddressSpace(1))(llvm)
      assert(p1.addressSpace == AddressSpace(1))
    }
  }

}
