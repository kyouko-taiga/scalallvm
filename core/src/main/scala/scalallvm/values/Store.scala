package scalallvm
package values

/** The storing of a value to memory. */
final class Store private[scalallvm] (val handle: LLVM.Handle) extends Instruction {

  /** `true` iff the store is marked volatile. */
  def isVolatile: Boolean =
    LLVM.StoreIsVolatile(handle)

  /** The alignment of the store. */
  def alignment: Int =
    LLVM.StoreGetAlignment(handle).toInt

  /** Sets the alignment of the store.
   *
   *  @param a The desired alignment, which is a power of two satisfying the alignment requirement
   *         of the type of the stored value.
   */
  def alignment_=(a: Int): Unit = {
    require((a & (a - 1)) == 0)
    LLVM.StoreSetAlignment(handle, a.toLong)
  }

}

object Store extends ValueConversion[Store] {

  def apply(source: Value): Option[Store] =
    LLVM.StoreFromValue(source.handle) match {
      case 0 => None
      case h => Some(new Store(h))
    }

}
