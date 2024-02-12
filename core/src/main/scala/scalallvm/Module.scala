package scalallvm

/** The top-level structure in an LLVM program. */
final class Module private (val handle: LLVM.Handle) extends LLVMObject with Disposable {

  /** Creates an instance with the given `name` in `context`. */
  def this(name: String, context: Context) =
    this(LLVM.ModuleCreateWithNameInContext(name, context.handle))

  /** A textual description of this module's IR. */
  def description: String =
    LLVM.ModuleDescription(handle)

  def dispose(): Unit =
    LLVM.ModuleDispose(handle)

}
