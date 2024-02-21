package scalallvm
package values

/** A poison value in LLVM IR. */
final class Poison private (val handle: LLVM.Handle) extends Constant {

  /** Creates a poison value of type `t`. */
  def this(t: types.Type) =
    this(LLVM.ConstantPoison(t.handle))

}
