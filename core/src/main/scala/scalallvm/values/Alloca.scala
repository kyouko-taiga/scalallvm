package scalallvm
package values

import types.Type

/** An instruction to allocate of memory on the stack. */
final class Alloca private[scalallvm] (val handle: LLVM.Handle) extends Instruction {

  /** `true` if the allocation is in an entry block and has a constant size. */
  def isStatic: Boolean =
    LLVM.AllocaIsStatic(handle)

  /** The type of the allocation. */
  def allocatedType: Type = {
    val h = LLVM.AllocaGetAllocatedType(handle)
    new Type { val handle = h }
  }

  /** The adress space of the allocation. */
  def addressSpace: AddressSpace =
    new AddressSpace(LLVM.AllocaGetAddressSpace(handle))

  /** The number of elements allocated. */
  def allocationCount: Value = {
    val h = LLVM.AllocaGetAllocationCount(handle)
    new Value { val handle = h }
  }

  /** The alignment of the allocation. */
  def alignment: Int =
    LLVM.AllocaGetAlignment(handle).toInt

  /** Sets the alignment of the allocation.
   *
   *  @param a The desired alignment, which is a power of two satisfying the alignment requirement
   *         of the allocated type.
   */
  def alignment_=(a: Int): Unit = {
    require((a & (a - 1)) == 0)
    LLVM.AllocaSetAlignment(handle, a.toLong)
  }

}

object Alloca extends ValueConversion[Alloca] {

  def apply(source: Value): Option[Alloca] =
    LLVM.AllocaFromValue(source.handle) match {
      case 0 => None
      case h => Some(new Alloca(h))
    }

}
