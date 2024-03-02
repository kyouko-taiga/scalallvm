package scalallvm

/** The specification of a platform on which code runs. */
final class Target private (val handle: LLVM.Handle) extends LLVMObject {

  /** The name of the target. */
  def name: String =
    LLVM.TargetGetName(handle)

  override def toString(): String =
    name

}

object Target {

  /** Creates an instance from a triple. */
  def apply(triple: String): Target = {
    LLVM.TargetFromTriple(triple) match {
      case 0 => throw new RuntimeException("cannot initialize target")
      case h =>
        if ((h & 1L) == 0) {
          new Target(h)
        } else {
          throw new RuntimeException(LLVM.ConsumeError(h))
        }
    }
  }

  /** Returns a triple describing the host. */
  lazy val hostTriple: String = {
    ensureNativeTargetIsInitialized()
    LLVM.DefaultTargetTriple()
  }

  /** Initializes the native target if necessary. */
  private def ensureNativeTargetIsInitialized(): Unit = {
    this.synchronized {
      if (!nativeTargetIsInitialized) {
        LLVM.InitializeTargets()
        nativeTargetIsInitialized = true
      }
    }
  }

  /** `true` iff the native target is initialized. */
  private var nativeTargetIsInitialized = false

}
