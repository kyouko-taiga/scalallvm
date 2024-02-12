package scalallvm

/** An opaque object that owns core LLVM data structures. */
final class Context private (val handle: LLVM.Handle) extends LLVMObject with Disposable {

  /** Creates a new instance. */
  def this() =
    this(LLVM.ContextCreate())

  def dispose(): Unit =
    LLVM.ContextDispose(handle)

}
