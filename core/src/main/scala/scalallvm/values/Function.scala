package scalallvm
package values

import support.ContainerView

/** A function in LLVM IR. */
final class Function private[scalallvm] (val handle: LLVM.Handle) extends Constant {

  /** The name of this function. */
  def name: String =
    LLVM.ValueGetName(handle)

  /** The parameters of the function. */
  def parameters: Function.Parameters =
    new Function.Parameters(this)

}

object Function {

  final class Parameter private[Function] (val handle: LLVM.Handle) extends Value

  /** A view on the parameters of a function.
   *
   *  @param base The function type containing the members in `this`.
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
