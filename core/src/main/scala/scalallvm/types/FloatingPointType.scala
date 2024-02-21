package scalallvm
package types

/** A floating-point type in LLVM IR.
 *
 *  @param bitWidth The number of bits in the representation of the type's instances.
 */
final class FloatingPointType private (val handle: LLVM.Handle) extends Type {

  /** Returns a constant whose LLVM IR type is `this` and whose value is `value`.
   */
  def apply(value: Double): values.FloatingPoint =
    new values.FloatingPoint(this, value)

}

object FloatingPointType {

  /** Returns `t` as an instance of `FloatingPointType` if it has the same kind. */
  def apply(t: Type): Option[FloatingPointType] =
    t match {
      case u if t.kind == TypeKind.double =>
        Some(new FloatingPointType(t.handle))
      case u if t.kind == TypeKind.float =>
        Some(new FloatingPointType(t.handle))
      case _ =>
        None
    }

  /** Returns LLVM's `float` type in `context`. */
  def float(implicit context: Context): FloatingPointType =
    new FloatingPointType(LLVM.FloatTypeInContext(context.handle))

  /** Returns LLVM's `double` type in `context`. */
  def double(implicit context: Context): FloatingPointType =
    new FloatingPointType(LLVM.DoubleTypeInContext(context.handle))

}