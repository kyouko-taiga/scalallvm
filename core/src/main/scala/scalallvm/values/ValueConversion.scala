package scalallvm.values

/** The checked conversion of the type of a value reference. */
trait ValueConversion[Self <: Value] {

  /** Returns `source` if it refers to an instance of `Self`. Otherwise, returns `None`. */
  def apply(source: Value): Option[Self]

}
