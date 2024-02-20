package scalallvm
package types

/** The `void` type of LLVM IR. */
final class VoidType private (val handle: LLVM.Handle) extends Type {

  /// Creates an instance in `context`.
  def this(context: Context) =
    this(LLVM.VoidTypeInContext(context.handle))

}
