package scalallvm

import com.github.sbt.jni.nativeLoader

@nativeLoader("scalallvm0")
object LLVM {

  /** A handle to a resource used by LLVM. */
  type Handle = Long

  // --- Context --------------------------------------------------------------

  @native def ContextCreate(): Handle
  @native def ContextDispose(self: Handle)

  // --- Module ---------------------------------------------------------------

  @native def ModuleCreateWithNameInContext(name: String, context: Handle): Handle
  @native def ModuleDescription(self: Handle): String
  @native def ModuleGetName(self: Handle): String
  @native def ModuleDispose(self: Handle)
  @native def ModuleSetName(self: Handle, name: String)

  // --- Types ----------------------------------------------------------------

  @native def IntTypeInContext(bitWidth: Int, context: Handle): Handle

  @native def PointerTypeInContext(space: Int, context: Handle): Handle
  @native def PointerTypeGetAddressSpace(self: Handle): Int

  @native def VoidTypeInContext(context: Handle): Handle

  // --- Values ---------------------------------------------------------------

  @native def ValueDescription(self: Handle, isForDebug: Boolean): String
  @native def ValueGetType(self: Handle): Handle

  @native def ConstantNullValue(tpe: Handle): Handle

  @native def ConstantIntCreate(tpe: Handle, value: Long, signed: Boolean): Handle
  @native def ConstantIntCreateWide(
    context: Handle, bitWidth: Int, words: Array[Long], signed: Boolean): Handle
  @native def ConstantIntGetSExtValue(tpe: Handle): Long
  @native def ConstantIntGetZExtValue(tpe: Handle): Long

  // --- Support --------------------------------------------------------------

}
