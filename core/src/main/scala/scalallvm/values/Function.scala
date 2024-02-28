package scalallvm
package values

import support.ContainerView
import scala.collection.immutable.ArraySeq

/** A function in LLVM IR. */
final class Function private[scalallvm] (val handle: LLVM.Handle) extends Constant {

  /** The name of this function. */
  def name: String =
    LLVM.ValueGetName(handle)

  /** The parameters of the function. */
  def parameters: Function.Parameters =
    new Function.Parameters(this)

  /** The entry of the function, if any. */
  def entry: Option[BasicBlock] =
    LLVM.FunctionBasicBlockEntry(handle) match {
      case 0 => None
      case h => Some(new BasicBlock(h))
    }

  /** The basic blocks in the function. */
  def basicBlocks: ArraySeq[BasicBlock] =
    LLVM.FunctionBasicBlocks(handle).map((h) => new BasicBlock(h)).to(ArraySeq)

  /** Adds a new basic block at the end of this function and returns it.
   *
   *  @param name The name of the block. A default value is generated if `name` is empty. If `name`
   *         already identifies a value in `this`, a fresh name is created using `name` as a prefix.
   */
  def appendBasicBlock(name: String = ""): BasicBlock = {
    val n = if (name.isEmpty()) { "bb" } else { name }
    new BasicBlock(LLVM.BasicBlockCreateInParent(n, 0, handle))
  }

  /** Adds a new basic block before `position` and returns it.
   *
   *  @param name The name of the block. A default value is generated if `name` is empty. If `name`
   *         already identifies a value in `this`, a fresh name is created using `name` as a prefix.
   */
  def insertBasicBlock(name: String = "", position: BasicBlock): BasicBlock = {
    require(position.parent == this)
    val n = if (name.isEmpty()) { "bb" } else { name }
    new BasicBlock(LLVM.BasicBlockCreateInParent(n, position.handle, handle))
  }

  /** Checks the function for errors and returns `Some(s)` if one was found, where `s` is a message
   *  describing that error. Otherwise, returns `None`.
   */
  def errors(): Option[String] =
    LLVM.FunctionVerifiy(handle) match {
      case null => None
      case s => Some(s)
    }

}

object Function {

  /** A parameter (aka formal argument) of a function. */
  final class Parameter private[Function] (val handle: LLVM.Handle) extends Value

  /** A view on the parameters of a function.
   *
   *  @param base The function containing the members in `this`.
   */
  final class Parameters(val base: Function) extends ContainerView[Parameter, LLVM.Handle] {

    // Note: We use handles as positions because iterators over an IR function's parameters are
    // represented as pointers to `llvm::Argument`. These pointers refer to an actual instance iff
    // they are within the bounds delimited by `arg_begin()` and `arg_end()`.

    val startPosition: LLVM.Handle =
      LLVM.FunctionParameterBegin(base.handle)

    val endPosition: LLVM.Handle =
      LLVM.FunctionParameterEnd(base.handle)

    def positionAfter(p: LLVM.Handle): LLVM.Handle =
      LLVM.FunctionParameterAfter(base.handle, p)

    def elementAt(p: LLVM.Handle): Parameter = {
      require((startPosition <= p) && (p < endPosition))
      new Parameter(p)
    }

    override def length: Int =
      LLVM.FunctionParameterCount(base.handle)

  }

}
