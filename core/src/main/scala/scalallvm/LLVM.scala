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

}
