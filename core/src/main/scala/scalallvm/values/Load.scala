package scalallvm
package values

/** The loading of a value from memory. */
final class Load private[scalallvm] (val handle: LLVM.Handle) extends Instruction {

  /** `true` iff the load is marked volatile. */
  def isVolatile: Boolean =
    LLVM.LoadIsVolatile(handle)

  /** The alignment of the load. */
  def alignment: Int =
    LLVM.LoadGetAlignment(handle).toInt

  /** Sets the alignment of the load.
   *
   *  @param a The desired alignment, which is a power of two satisfying the alignment requirement
   *         of the type of the loaded value.
   */
  def alignment_=(a: Int): Unit = {
    require((a & (a - 1)) == 0)
    LLVM.LoadSetAlignment(handle, a.toLong)
  }

}

object Load extends ValueConversion[Load] {

  def apply(source: Value): Option[Load] =
    LLVM.LoadFromValue(source.handle) match {
      case 0 => None
      case h => Some(new Load(h))
    }

}
