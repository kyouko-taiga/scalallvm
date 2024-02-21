package scalallvm
package types

/** An array type in LLVM IR. */
final class ArrayType private (val handle: LLVM.Handle) extends Type {

  /** Creates an instance representing arrays of `count` `element`s in `context`. */
  def this(count: Int, element: Type)(implicit context: Context) =
    this(LLVM.ArrayTypeCreateInContext(count, element.handle, context.handle))

  /** The number of elements in the array. */
  val length: Int =
    LLVM.ArrayTypeCount(handle)

  /** The type of the elements in the array. */
  val element: Type = {
    val h = LLVM.ArrayTypeElement(handle)
    new Type { val handle = h }
  }

  override def kind: TypeKind =
    TypeKind.array

}

object ArrayType {

  def apply(t: Type): Option[ArrayType] =
    if (t.kind == TypeKind.array) { Some(new ArrayType(t.handle)) } else { None }

}
