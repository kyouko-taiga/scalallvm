package scalallvm

/** The settings necessary for code generation. */
final class TargetMachine private (val handle: LLVM.Handle) extends LLVMObject with Disposable {

  /** Returns the result of calling `action` on the data layout of this target.
   *
   *  The argument to `action` is only valid for the duration of `action`'s call. It is undefined
   *  behavior to let it escape in any way.
   */
  def withDataLayout[R](action: DataLayout => R): R = {
    val l = new DataLayout(LLVM.DataLayoutFromTargetMachine(handle))
    try action(l) finally l.dispose()
  }

  def dispose(): Unit =
    LLVM.TargetMachineDispose(handle)

}

object TargetMachine {

  /** Creates an instance with the given properties.
   *
   *  @param triple The triple identifying the platform for which code is generated.
   *  @param cpu The type of CPU to target, empty for the defaults of the host machine.
   *  @param features The features a of the target, empty for the defaults of the host machine.
   *  @param optimization The level of optimization used during code generation.
   *  @param relocationModel The relocation model used during code generation.
   *  @param codeModel The code model used during code generation.
   */
  def withNew[R](
      triple: String = Target.hostTriple,
      cpu: String = "",
      features: String = "",
      optimization: OptimizationLevel = OptimizationLevel.O0,
      relocationModel: RelocationModel = RelocationModel.default,
      codeModel: CodeModel = CodeModel.default
  )(action: TargetMachine => R): R = {
    val target = Target(triple)
    val h = LLVM.TargetMachineCreate(
      target.handle, triple, cpu, features,
      optimization.rawValue, relocationModel.rawValue, codeModel.rawValue)
    val m = new TargetMachine(h)
    try action(m) finally m.dispose()
  }

}
