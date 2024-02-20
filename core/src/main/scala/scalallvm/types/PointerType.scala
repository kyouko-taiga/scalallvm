package scalallvm
package types

/** The `ptr` type of LLVM IR. */
final class PointerType private (val handle: LLVM.Handle) extends Type {

  /** Creates an instance in `context` in address space `s`. */
  def this(s: AddressSpace = AddressSpace.default)(implicit context: Context) =
    this(LLVM.PointerTypeInContext(s.rawValue, context.handle))

  /** The adress space of this pointer type. */
  def addressSpace: AddressSpace =
    new AddressSpace(LLVM.PointerTypeGetAddressSpace(handle))

}
