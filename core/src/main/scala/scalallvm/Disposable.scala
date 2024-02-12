package scalallvm

/** A reference to an object that can be manually disposed. */
trait Disposable {

  /** Destroys the referenced object.
   *
   *  Using this reference after `dispose` has been called has undefined behavior.
   */
  def dispose(): Unit

}
