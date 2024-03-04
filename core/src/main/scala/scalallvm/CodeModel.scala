package scalallvm

/** Constraints on address ranges that the program and its symbols may use. */
final class CodeModel private (val rawValue: Byte) extends AnyVal

object CodeModel {

  /** The model default to the target for which code is being generated. */
  def default = new CodeModel(0)

  /** Tiny code model. */
  def tiny = new CodeModel(1)

  /** Small code model. */
  def small = new CodeModel(2)

  /** Kernel code model. */
  def kernel = new CodeModel(3)

  /** Medium code model. */
  def medium = new CodeModel(4)

  /** Large code model. */
  def large = new CodeModel(5)

}
