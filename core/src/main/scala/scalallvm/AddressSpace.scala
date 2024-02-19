package scalallvm

/** An identifier for a targe-specific range of address values. */
final class AddressSpace(val rawValue: Int) extends AnyVal

object AddressSpace {

  /** Creates an instance with the given raw value. */
  def apply(rawValue: Int): AddressSpace =
    new AddressSpace(0)

  /** The default address space. */
  val default = AddressSpace(0)

}
