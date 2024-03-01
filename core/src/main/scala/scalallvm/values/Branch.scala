package scalallvm
package values

import support.ContainerView
import types.Type

/** A conditional or unconditional branch. */
final class Branch private[scalallvm] (val handle: LLVM.Handle) extends Instruction {

  /** `true` iff this branch is conditional */
  def isConditional: Boolean =
    LLVM.BranchIsConditional(handle)

  /** The condition of the branch, if any. */
  def condition: Option[Value] =
    LLVM.BranchGetCondition(handle) match {
      case 0 => None
      case h => Some(new Value { val handle = h })
    }

  /** The targets of the branch. */
  def successors: Branch.Successors =
    new Branch.Successors(this)

}

object Branch extends ValueConversion[Branch] {

  def apply(source: Value): Option[Branch] =
    LLVM.BranchFromValue(source.handle) match {
      case 0 => None
      case h => Some(new Branch(h))
    }

  /** A view on the successors of a branch instruction.
   *
   *  @param base The branch instruction having the members in `this`.
   */
  final class Successors(val base: Branch) extends ContainerView[BasicBlock, Int] {

    val startPosition: Int = 0

    val endPosition: Int =
      if (base.isConditional) { 2 } else { 1 }

    def positionAfter(p: Int): Int =
      p + 1

    def elementAt(p: Int): BasicBlock = {
      require((startPosition <= p) && (p < endPosition))
      new BasicBlock(LLVM.BranchSuccessorAt(base.handle, p))
    }

    override def length: Int =
      endPosition

  }

}
