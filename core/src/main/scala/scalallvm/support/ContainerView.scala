package scalallvm.support

import scala.annotation.tailrec
import scala.collection.immutable.LinearSeq

/** An immutable view of the contents of a container.
 *
 *  A container is a finite sequence of elements that can be accessed in constant time via their
 *  position (see C++'s `Container` concept).
 */
trait ContainerView[Element, Position] extends LinearSeq[Element] {

  /** The position of the first element in the view. */
  def startPosition: Position

  /** The position after the last element in the view. */
  def endPosition: Position

  /** Returns the position immediately after `p`. */
  def positionAfter(p: Position): Position

  /** Returns the element at position `p` in the view. */
  def elementAt(p: Position): Element

  /** `true` if this view is emty. */
  override def isEmpty: Boolean =
    startPosition == endPosition

  override def iterator: ContainerViewIterator[Element, Position] =
    new ContainerViewIterator(this, startPosition)

}

/** An iterator over the contents of an `ContainerView`. */
final class ContainerViewIterator[Element, Position](
    val base: ContainerView[Element, Position],
    var position: Position
) extends Iterator[Element] {

  def hasNext: Boolean =
    position != base.endPosition

  def next(): Element = {
    val result = base.elementAt(position)
    position = base.positionAfter(position)
    result
  }

  override def equals(other: Any): Boolean =
    other match {
      case rhs: ContainerViewIterator[_, _] =>
        (base == rhs.base) && (position == rhs.position)
      case _ =>
        false
    }

  override def hashCode(): Int =
    base.hashCode() ^ position.hashCode()

}
