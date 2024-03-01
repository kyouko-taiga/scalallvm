package scalallvm

import types.Type
import values.{Instruction, Value}
import scalallvm.values.BasicBlock

/** The API to create LLVM IR instructions and insert them into basic blocks. */
final class InstructionBuilder private (
    val handle: LLVM.Handle
) extends LLVMObject with Disposable {

  /** Creates a new instance in the given `context`. */
  private[scalallvm] def this(context: Context) =
    this(LLVM.InstructionBuilderCreateInContext(context.handle))

  /** Configures the builder so that it inserts new instruction at the end of `b`. */
  def positionAtEndOf(b: BasicBlock): Unit =
    LLVM.InstructionBuilderPositionAtEndOfBlock(handle, b.handle)

  /** Creates an `alloca` with the given properties. */
  def alloca(
    tpe: Type,
    space: AddressSpace = AddressSpace.default,
    size: Option[Value] = None,
    name: String = ""
  ): values.Alloca = {
    val v = size.map(_.handle).getOrElse(0L)
    val h = LLVM.InstructionBuilderMakeAlloca(handle, tpe.handle, space.rawValue, v, name)
    new values.Alloca(h)
  }

  def dispose(): Unit =
    LLVM.InstructionBuilderDispose(handle)

}
