package scalallvm

/** An opaque object that owns core LLVM data structures. */
final class Context private (val handle: LLVM.Handle) extends LLVMObject with Disposable {

  /** Creates a new instance. */
  def this() =
    this(LLVM.ContextCreate())

  /** Returns the result of calling `action` with a new LLVM module named `n`.
   *
   *  The argument to `action` is only valid for the duration of action's `call`. It is undefined
   *  behavior to let it escape in any way.
   */
  def withNewModule[R](n: String)(action: Module => R): R = {
    val instance = new Module(n, this)
    try action(instance) finally instance.dispose()
  }

  def dispose(): Unit =
    LLVM.ContextDispose(handle)

}

object Context {

  /** Returns the result of calling `action` with a new LLVM context.
   *
   *  The argument to `action` is only valid for the duration of action's `call`. It is undefined
   *  behavior to let it escape in any way.
   */
  def withNew[R](action: Context => R): R = {
    val instance = new Context()
    try action(instance) finally instance.dispose()
  }

}
