package scalallvm
package values

import support.WideInteger
import types.IntegerType

/** A constant integer value in LLVM IR. */
final class Integer private (val handle: LLVM.Handle) extends Value {

  /** Creates an instance whose LLVM IR type is `tpe` and whose value is `value` truncated or
   *  sign-extended if needed to fit `tpe.bitWidth`.
   */
  def this(tpe: IntegerType, value: Int) =
    this(LLVM.ConstantIntCreate(tpe.handle, value.toLong, true))

  /** Creates an instance whose LLVM IR with the given `value` in `context`.
   *
   *  The type of the result corresponds to bit width the properties of `value`. For example, if
   *  `value` is a 8-bit signed integer, the type of the result is `i8`.
   */
  def this(value: WideInteger)(implicit context: Context) =
    this(LLVM.ConstantIntCreateWide(context.handle, value.bitWidth, value._words, value.isSigned))

  /** The value of this cons tant as a 64-bit integer after it's been sign-extended. */
  def sext: Long =
    LLVM.ConstantIntGetSExtValue(handle)

  /** The value of this constant as a 64-bit unsigned integer after it's been zero-extended. */
  def zext: Long =
    LLVM.ConstantIntGetSExtValue(handle)

}
