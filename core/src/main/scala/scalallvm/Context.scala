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

  /** LLVM's `void` type. */
  def void = new types.VoidType(this)

  /** LLVM's `ptr` type, in the default address space. */
  def ptr = new types.PointerType(this)

  /** LLVM's `i1` type. */
  def i1 = new types.IntegerType(1, this)

  /** LLVM's `i8` type. */
  def i8 = new types.IntegerType(8, this)

  /** LLVM's `i16` type. */
  def i16 = new types.IntegerType(16, this)

  /** LLVM's `i32` type. */
  def i32 = new types.IntegerType(32, this)

  /** LLVM's `i64` type. */
  def i64 = new types.IntegerType(64, this)

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
