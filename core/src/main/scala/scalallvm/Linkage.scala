package scalallvm

/** How names can or cannot be referred to. */
final class Linkage(val rawValue: Byte) extends AnyVal

object Linkage {

  /** The name is externally visible; it participates in linkage and can be used to resolve
   *  external symbol references.
   */
  lazy val external = new Linkage(LLVM.LinkageExternal())

}
