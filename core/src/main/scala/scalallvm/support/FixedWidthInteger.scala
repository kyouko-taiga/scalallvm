package scalallvm.support

import scala.collection.immutable.ArraySeq

/** A class describing types that can represent fixed-size binary integers. */
trait FixedWidthInteger[Self] {

  /** Returns the words `self`'s representation', from the least to most significant.
   *
   *  Negative values are returned in two's complement. If `bitWidth` is smaller than `WORD_WIDTH`,
   *  the result contains one word obtained by sign-extending the representation of this value.
   */
  def words(self: Self): IndexedSeq[Long]

  /** Returns `-1` if `self` is negative, `1` if `self` is positive, or `0` otherwise. */
  def signum(self: Self): Int

  /** Returns the number of leading zeros in `self`'s representation. */
  def numberOfLeadingZeros(self: Self): Int

  /** The number of bits in `self`'s representation. */
  def bitWidth(self: Self): Int

  /** `true` iff in `self`'s representation is signed. */
  def isSigned(self: Self): Boolean

}

object FixedWidthInteger {

  /** The number of bits in a single word of a `FixedWidthInteger`. */
  final val WORD_WIDTH = 64

  /** The conformance of a 32-bit signed integer to `FixedWidthInteger`. */
  implicit val i32: FixedWidthInteger[Int] = new FixedWidthInteger[Int] {

    def words(self: Int): IndexedSeq[Long] =
      ArraySeq(self.toLong)

    def signum(self: Int): Int =
      math.signum(self)

    def numberOfLeadingZeros(self: Int): Int =
      Integer.numberOfLeadingZeros(self)

    def bitWidth(self: Int): Int =
      32

    def isSigned(self: Int): Boolean =
      false

  }

  /** The conformance of a 64-bit unsigned integer to `FixedWidthInteger`. */
  val u32: FixedWidthInteger[Int] = new FixedWidthInteger[Int] {

    def words(self: Int): IndexedSeq[Long] =
      ArraySeq(Integer.toUnsignedLong(self))

    def signum(self: Int): Int =
      if (self == 0) { 0 } else { 1 }

    def numberOfLeadingZeros(self: Int): Int =
      Integer.numberOfLeadingZeros(self)

    def bitWidth(self: Int): Int = 32

    def isSigned(self: Int): Boolean = false

  }

  /** The conformance of a 64-bit signed integer to `FixedWidthInteger`. */
  implicit val i64: FixedWidthInteger[Long] = new FixedWidthInteger[Long] {

    def words(self: Long): IndexedSeq[Long] =
      ArraySeq(self)

    def signum(self: Long): Int =
      math.signum(self).toInt

    def numberOfLeadingZeros(self: Long): Int = {
      if (self < 0) {
        0
      } else if (self > 0xffffffffL) {
        Integer.numberOfLeadingZeros((self >>> 32).toInt)
      } else {
        Integer.numberOfLeadingZeros(self.toInt) + 32
      }
    }

    def bitWidth(self: Long): Int =
      64

    def isSigned(self: Long): Boolean =
      false
  }

  /** The conformance of a wide integer to `FixedWidthInteger`. */
  implicit val wide: FixedWidthInteger[WideInteger] = new FixedWidthInteger[WideInteger] {

    def words(self: WideInteger): IndexedSeq[Long] =
      self.words

    def signum(self: WideInteger): Int =
      self.signum

    def numberOfLeadingZeros(self: WideInteger): Int =
      self.numberOfLeadingZeros

    def bitWidth(self: WideInteger): Int =
      self.bitWidth

    def isSigned(self: WideInteger): Boolean =
      self.isSigned

  }

}
