package scalallvm

/** The top-level structure in an LLVM program. */
final class Module private (val handle: LLVM.Handle) extends LLVMObject with Disposable {

  /** Creates an instance with the given `name` in `context`. */
  def this(name: String, context: Context) =
    this(LLVM.ModuleCreateWithNameInContext(name, context.handle))

  /** The name of this module. */
  def name: String =
    LLVM.ModuleGetName(handle)

  /** Sets the name of this module. */
  def name_=(n: String): Unit =
    LLVM.ModuleSetName(handle, n)

  /** A textual description of this module's IR. */
  def description: String =
    LLVM.ModuleDescription(handle)

  def dispose(): Unit =
    LLVM.ModuleDispose(handle)

}
