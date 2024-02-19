package scalallvm
package values

/** A value in LLVM IR. */
trait Value extends LLVMObject {

  /** The LLVM IR type of this value. */
  def tpe: types.Type = {
    val h = LLVM.ValueGetType(handle)
    new types.Type { val handle = h }
  }

  override def toString(): String =
    LLVM.ValueDescription(handle, false)

}
