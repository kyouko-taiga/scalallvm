package scalallvm
package types

/** The `void` type of LLVM IR. */
final class VoidType private (val handle: LLVM.Handle) extends Type {

  /// Creates an instance in `context`.
  def this(context: Context) =
    this(LLVM.VoidTypeInContext(context.handle))

  override def kind: TypeKind =
    TypeKind.void

}

object VoidType {

  def apply(t: Type): Option[VoidType] =
    if (t.kind == TypeKind.void) { Some(new VoidType(t.handle)) } else { None }

}
