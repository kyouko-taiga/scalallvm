package scalallvm

/** The kind of result produced by code generation. */
final class CodeGenerationKind private (val rawValue: Byte) extends AnyVal

object CodeGenerationKind {

  /** Assembly. */
  val assembly = new CodeGenerationKind(0)

  /** Object code. */
  val objectCode = new CodeGenerationKind(1)

}
