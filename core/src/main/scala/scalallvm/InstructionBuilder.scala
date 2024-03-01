package scalallvm

import types.Type
import values.{BasicBlock, Instruction, Value}

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

  /** Retutns the basic block in which new instruction are being inserted. */
  def insertionBlock: Option[BasicBlock] =
    LLVM.InstructionBuilderGetInsertionBlock(handle) match {
      case 0 => None
      case h => Some(new BasicBlock(h))
    }

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

  /** Creates an unconditional `br` with the given properties. */
  def br(destination: BasicBlock): values.Branch = {
    val h = LLVM.InstructionBuilderMakeBr(handle, destination.handle)
    new values.Branch(h)
  }

  /** Creates a conditional `br` with the given properties. */
  def br(condition: Value, success: BasicBlock, failure: BasicBlock): values.Branch = {
    val h = LLVM.InstructionBuilderMakeConditionalBr(
      handle, condition.handle, success.handle, failure.handle)
    new values.Branch(h)
  }

  /** Creates a `getelementptr` with the given properties. */
  def getelementptr(
    base: Value,
    baseType: Type,
    indices: Seq[Value],
    inBounds: Boolean = false,
    name: String = ""
  ): Instruction = {
    val i = indices.map(_.handle).toArray
    val h = LLVM.InstructionBuilderMakeGEP(handle, base.handle, baseType.handle, i, name, inBounds)
    new Instruction { val handle = h }
  }

  /** Creates a `getelementptr` with the given properties. */
  def structelementptr(
    base: Value,
    baseType: types.StructType,
    index: Int,
    name: String = ""
  ): Instruction = {
    val h = LLVM.InstructionBuilderMakeStructGEP(handle, base.handle, baseType.handle, index, name)
    new Instruction { val handle = h }
  }

  /** Creates a `load` with the given properties. */
  def load(
      source: Value, sourceType: Type, isVolatile: Boolean = false, name: String = ""
  ): values.Load = {
    val h = LLVM.InstructionBuilderMakeLoad(
      handle, source.handle, sourceType.handle, isVolatile, name)
    new values.Load(h)
  }

  /** Creates a `ret void`. */
  def rturn(): Instruction = {
    val h = LLVM.InstructionBuilderMakeReturn(handle, 0)
    new Instruction { val handle = h }
  }

   /** Creates a `ret` with the given value. */
  def rturn(value: Value): Instruction = {
    val h = LLVM.InstructionBuilderMakeReturn(handle, value.handle)
    new Instruction { val handle = h }
  }

  /** Creates a `stroe` with the given properties. */
  def store(value: Value, target: Value, isVolatile: Boolean = false): values.Store = {
    val h = LLVM.InstructionBuilderMakeStore(handle, value.handle, target.handle, isVolatile)
    new values.Store(h)
  }

  /** Creates a `switch` with the given properties. */
  def switch(
      condition: Value, default: BasicBlock, cases: Seq[(values.Integer, BasicBlock)]
  ): values.Switch = {
    val v = cases.map(_._1.handle).toArray
    val d = cases.map(_._2.handle).toArray
    val h = LLVM.InstructionBuilderMakeSwitch(handle, condition.handle, default.handle, v, d)
    new values.Switch(h)
  }

  /** Creates a `trunc ... to` with the given properties. */
  def truncTo(source: Value, target: Type, name: String = ""): Instruction = {
    val h = LLVM.InstructionBuilderMakeTruncTo(handle, source.handle, target.handle, name)
    new Instruction { val handle = h }
  }

  /** Creates an `unrechable`. */
  def unreachable(): Instruction = {
    val h = LLVM.InstructionBuilderMakeUnreachable(handle)
    new Instruction { val handle = h }
  }

  def dispose(): Unit =
    LLVM.InstructionBuilderDispose(handle)

}
