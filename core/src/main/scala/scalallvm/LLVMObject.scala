package scalallvm

/** A reference to an LLVM object. */
trait LLVMObject {

  /** An opaque handle to the LLVM data structure. */
  def handle: LLVM.Handle

  override def equals(other: Any): Boolean =
    other match {
      case o: LLVMObject => handle == o.handle
      case _ => false
    }

  override def hashCode(): Int =
    handle.hashCode()

  override def toString: String = {
    val n = getClass.getSimpleName
    s"${n}(${handle.toHexString})"
  }

}
