package scalallvm
package types

/** The type of a value in LLVM IR. */
trait Type extends LLVMObject {

  /** The `null` instance of this type (e.g., the zero of `i32`). */
  def nll: values.Value = {
    val h = LLVM.ConstantNullValue(handle)
    new values.Value { val handle = h }
  }

}
