package scalallvm
package values

/** An undefined value in LLVM IR. */
final class Undefined private (val handle: LLVM.Handle) extends Constant {

  /** Creates an undefined value of type `t`. */
  def this(t: types.Type) =
    this(LLVM.ConstantUndefined(t.handle))

}
