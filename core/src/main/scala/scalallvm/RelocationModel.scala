package scalallvm

/** The settings of position-independent code (PIC) during code generation. */
final class RelocationModel private (val rawValue: Byte) extends AnyVal

object RelocationModel {

  /** The model default to the target for which code is being generated. */
  def default = new RelocationModel(0)

  /** Non-relocatable code.
   *
   *  Machine instructions may use absolute addressing modes.
   */
  def static = new RelocationModel(1)

  /** Fully relocatable position independent code (PIC).
   *
   *  Machine instructions need to use relative addressing modes.
   */
  def pic = new RelocationModel(2)

}
