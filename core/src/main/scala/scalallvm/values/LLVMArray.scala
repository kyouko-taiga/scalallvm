package scalallvm
package values

import support.ContainerView
import types.{ArrayType, Type}

/** A constant array in LLVM IR. */
final class LLVMArray private (val handle: LLVM.Handle) extends Constant {

  /** Creates an instance whose LLVM IR type is an array of `element` that aggregates `members`.
   *
   *  The LLVM IR type of all elements in `members` must be `element`.
   */
  def this(element: Type, members: Seq[Constant]) =
    this(LLVM.ConstantArray(element.handle, members.map(_.handle).toArray))

  /** The number of elements in this array. */
  def length: Int =
    ArrayType(tpe).get.length

  /** The members of this array. */
  def members: LLVMArray.Members =
    new LLVMArray.Members(this)

}

object LLVMArray {

  /** Creates an array of `i8` values with the utf8 contents of `text` in `context`, adding a null
   *  terminator iff `nullTerminated` is `true`.
   *
   *  @param text The payload of the returned array.
   *  @param nullTerminated `true` iff a null-terminator should be added to the contents of the
   *         returned array, increasing its length by one.
   */
  def string(text: String, nullTerminated: Boolean = true)(implicit context: Context): LLVMArray =
    new LLVMArray(LLVM.ConstantStringInContext(text, nullTerminated, context.handle))

  /** A view on the members of an array in LLVM.
   *
   *  @param base The array containing the members in `this`.
   */
  final class Members(val base: LLVMArray) extends ContainerView[Constant, Int] {

    val startPosition: Int = 0

    val endPosition: Int =
      base.length

    def positionAfter(p: Int): Int =
      p + 1

    def elementAt(p: Int): Constant = {
      require((startPosition <= p) && (p < endPosition))
      new Constant { val handle = LLVM.ConstantAggregateMemberAt(base.handle, p) }
    }

  }

}
