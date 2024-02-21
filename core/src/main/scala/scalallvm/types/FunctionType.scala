package scalallvm
package types

import support.ArrayView

/** The type of a function in LLVM IR. */
final class FunctionType private (val handle: LLVM.Handle) extends Type {

  /** Creates an instance with given `parameters` and `returnType`. */
  def this(parameters: Seq[Type], returnType: Type) =
    this(LLVM.FunctionType(parameters.map(_.handle).toArray, returnType.handle))

  /** The parameters of the function. */
  def parameters: FunctionType.Parameters =
    new FunctionType.Parameters(this)

  /** The return type of the function. */
  def returnType: Type = {
    val h = LLVM.FunctionTypeReturn(handle)
    new types.Type { val handle = h }
  }

  override def kind: TypeKind =
    TypeKind.function

}

object FunctionType {

  def apply(t: Type): Option[FunctionType] =
    if (t.kind == TypeKind.function) { Some(new FunctionType(t.handle)) } else { None }

  /** A view on the parameters of a function type.
   *
   *  @param base The function type containing the members in `this`.
  */
  final class Parameters(val base: FunctionType) extends ArrayView[Type] {

    /** The position of the first element in this view. */
    val startPosition: Int = 0

    /** The position after the last element in this view. */
    val endPosition: Int = LLVM.FunctionTypeParameterCount(base.handle)

    /** Accesses the element at position `p`. */
    def elementAt(p: Int): Type = {
      require((startPosition <= p) && (p < endPosition))
      new Type { val handle = LLVM.FunctionTypeParameterAt(base.handle, p) }
    }

  }

}
