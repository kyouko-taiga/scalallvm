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
  final class Parameters(val base: Function) extends ContainerView[Parameter, Int] {

    val startPosition: Int = 0

    val endPosition: Int = LLVM.FunctionParameterCount(base.handle)

    def positionAfter(p: Int): Int =
      p + 1

    def elementAt(p: Int): Parameter = {
      require((startPosition <= p) && (p < endPosition))
      new Parameter(LLVM.FunctionParameterAt(base.handle, p))
    }

  }

}
