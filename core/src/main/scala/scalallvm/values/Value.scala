package scalallvm
package values

/** A value in LLVM IR. */
trait Value extends LLVMObject {

  /** The LLVM IR type of this value. */
  def tpe: types.Type = {
    val h = LLVM.ValueGetType(handle)
    new types.Type { val handle = h }
  }

  /** The name of this value. */
  def name: String =
    LLVM.ValueGetName(handle)

  /** A textual description of this value's IR. */
  def description: String =
    LLVM.ValueDescription(handle, false)

  override def toString(): String =
    name

}
