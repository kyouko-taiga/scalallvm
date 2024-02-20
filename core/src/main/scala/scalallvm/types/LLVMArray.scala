package scalallvm
package types

/** An array type in LLVM IR. */
final class LLVMArray private (val handle: LLVM.Handle) extends Type {

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

}
