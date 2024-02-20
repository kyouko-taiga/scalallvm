package scalallvm.support

import scala.collection.immutable.LinearSeq

/** An immutable view of the contents of an array or slice thereof. */
trait ArrayView[Element] extends LinearSeq[Element] {

  /** The position of the first element in the view. */
  def startPosition: Int

  /** The position after the last element in the view. */
  def endPosition: Int

  /** Returns the element at position `p` in the view. */
  def elementAt(p: Int): Element

  /** The number of elements in this view. */
  override def length: Int =
    endPosition

  /** `true` if this view is emty. */
  override def isEmpty: Boolean =
    startPosition == endPosition

  /** Accesses the element at index `i`. */
  override def apply(i: Int): Element =
    elementAt(startPosition + i)

}

/** An iterator over the contents of an `ArrayView`. */
final class ArrayViewIterator[Element, Base <: ArrayView[Element]](
    val base: Base
) extends Iterator[Element] {

  /** The current position of the iterator. */
  var position: Int = 0

  def hasNext: Boolean =
    position != base.endPosition

  def next(): Element = {
    val result = base.elementAt(position)
    position = position + 1
    result
  }

  override def equals(other: Any): Boolean =
    other match {
      case rhs: ArrayViewIterator[_, _] =>
        (base == rhs.base) && (position == rhs.position)
      case _ =>
        false
    }

  override def hashCode(): Int =
    base.hashCode() ^ position.hashCode()

}
