package scalallvm
package values

import support.ContainerView
import types.Type

/** A multiway branch. */
final class Switch private[scalallvm] (val handle: LLVM.Handle) extends Instruction {

  /** The condition of the branch, if any. */
  def condition: Value = {
    val h = LLVM.SwitchGetCondition(handle)
    new Value { val handle = h }
  }

  /** The default target of the branch (i.e., the target if none of the pattern matches). */
  def defaultSuccessor: BasicBlock =
    new BasicBlock(LLVM.SwitchGetDefaultDestination(handle))

  /** The targets of the branch. */
  def successors: Switch.Successors =
    new Switch.Successors(this)

}

object Switch extends ValueConversion[Switch] {

  def apply(source: Value): Option[Switch] =
    LLVM.SwitchFromValue(source.handle) match {
      case 0 => None
      case h => Some(new Switch(h))
    }

  /** A view on the successors of a switch instruction.
   *
   *  @param base The switch instruction having the members in `this`.
   */
  final class Successors(val base: Switch) extends ContainerView[BasicBlock, Int] {

    val startPosition: Int = 0

    val endPosition: Int =
      LLVM.SwitchSuccessorCount(base.handle)

    def positionAfter(p: Int): Int =
      p + 1

    def elementAt(p: Int): BasicBlock = {
      require((startPosition <= p) && (p < endPosition))
      new BasicBlock(LLVM.SwitchSuccessorAt(base.handle, p))
    }

    override def length: Int =
      endPosition

  }

}
