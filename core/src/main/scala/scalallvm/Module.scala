package scalallvm

import types.FunctionType
import values.Function

/** The top-level structure in an LLVM program. */
final class Module private (val handle: LLVM.Handle) extends LLVMObject with Disposable {

  /** Creates an instance with the given `name` in `context`. */
  def this(name: String)(implicit context: Context) =
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

  /** Returns a function named `n` and having type `t`, declaring it if it doesn't exist. */
  def declareFunction(
      n: String, t: FunctionType,
      l: Linkage = Linkage.external,
      s: AddressSpace = AddressSpace.default
  ): Function =
    function(n) match {
      case Some(f) => f
      case None =>
        new Function(LLVM.FunctionCreateInModule(n, t.handle, l.rawValue, s.rawValue, handle))
    }

  /** Returns the function named `n` in this module, if any. */
  def function(n: String): Option[Function] =
    LLVM.FunctionGetByNameInModule(n, handle) match {
      case 0 => None
      case h => Some(new Function(h))
    }

  def dispose(): Unit =
    LLVM.ModuleDispose(handle)

}

object Module {

  /** Returns the result of calling `action` with a new module named `n`, in a new context.
   *
   *  The arguments to `action` are only valid for the duration of action's `call`. It is undefined
   *  behavior to let them escape in any way.
   */
  def withNew[R](n: String)(action: (Context, Module) => R): R =
    Context.withNew((c) => c.withNewModule(n)((m) => action(c, m)))

}