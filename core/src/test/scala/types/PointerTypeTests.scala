package types

import scalallvm.{AddressSpace, Context}
import scalallvm.types

class PointerTests extends munit.FunSuite {

  test("addressSpace") {
    Context.withNew { (llvm) =>
      val p0 = new types.PointerType(llvm)
      assert(p0.addressSpace == AddressSpace.default)
      val p1 = new types.PointerType(llvm, AddressSpace(1))
      assert(p1.addressSpace == AddressSpace(1))
    }
  }

}
