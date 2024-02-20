package scalallvm
package types

/** An integer type in LLVM IR.
 *
 *  @param bitWidth The number of bits in the representation of the type's instances.
 */
final class IntegerType private (val bitWidth: Int, val handle: LLVM.Handle) extends Type {

  require(bitWidth > 0)

  /** Creates an instance with the given `bitWidth` in `context`. */
  def this(bitWidth: Int, context: Context) =
    this(bitWidth, LLVM.IntTypeInContext(bitWidth, context.handle))

  /** Returns a constant whose LLVM IR type is `this` and whose value is `value`, truncated or
   *  sign-extended if needed to fit `bitWidth`.
   */
  def apply(value: Int): values.Integer =
    new values.Integer(this, value)

  /** An instance of this type equal to 0. */
  def zero: values.Integer =
    new values.Integer(this, 0)

}
