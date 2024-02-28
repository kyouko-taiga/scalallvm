package scalallvm
package values

/** A basic block in LLVM IR. */
final class BasicBlock private[scalallvm] (val handle: LLVM.Handle) extends Constant {

    /** The name of this block. */
  def name: String =
    LLVM.ValueGetName(handle)

  /** Returns the function containing this block. */
  def parent: Function =
    new Function(LLVM.BasicBlockGetParent(handle))

}
