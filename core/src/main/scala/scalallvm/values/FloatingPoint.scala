package scalallvm
package values

import types.FloatingPointType

/** A constant floating-point value in LLVM IR. */
final class FloatingPoint private (val handle: LLVM.Handle) extends Value {

  /** Creates an instance whose LLVM IR type is `tpe` and whose value is `value` truncated or
   *  sign-extended if needed to fit `tpe.bitWidth`.
   */
  def this(tpe: FloatingPointType, value: Double) =
    this(LLVM.ConstantDouble(tpe.handle, value))

}
