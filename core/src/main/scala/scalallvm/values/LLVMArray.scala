package scalallvm
package values

import support.ArrayView
import types.{ArrayType, Type}

/** A constant array in LLVM IR. */
final class LLVMArray private (val handle: LLVM.Handle) extends Constant {

  /** Creates an instance whose LLVM IR type is an array of `element` that aggregates `members`.
   *
   *  The LLVM IR type of all elements in `members` must be `element`.
   */
  def this(element: Type, members: Seq[Constant]) =
    this(LLVM.ConstantArray(element.handle, members.map(_.handle).toArray))

  /** The members of this array. */
  def members: ArrayConstant.Members =
    new ArrayConstant.Members(this)

}

object ArrayConstant {

  /** A view on the members of an array in LLVM.
   *
   *  @param base The array containing the members in `this`.
   */
  final class Members(val base: LLVMArray) extends ArrayView[Constant] {

    val startPosition: Int = 0

    val endPosition: Int =
      ArrayType(base.tpe).get.length

    def elementAt(p: Int): Constant = {
      require((startPosition <= p) && (p < endPosition))
      new Constant { val handle = LLVM.ConstantAggregateMemberAt(base.handle, p) }
    }

  }

}
