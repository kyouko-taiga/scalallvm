package scalallvm.support

import java.util.Arrays
import scala.annotation.tailrec
import scala.collection.immutable.ArraySeq
import scala.math

/** An integer with a binary representation.
 *
 *  This type serves as a utility to perform conversions representations of binary integers (e.g.
 *  to convert a 32-bit signed value to a 16-bit unsigned one). It also replaces `llvm::APInt` in
 *  wrapped APIs.
 *
 *  @param _words The words in the instance's representation, from least to most significant.
 *  @param bitWidth The number of significant bits used in the instance's representation.
 *  @param isSigned `true` iff the instance's representation is signed.
 */
final class WideInteger(
    private[scalallvm] val _words: Array[Long], val bitWidth: Int, val isSigned: Boolean
) {

  import FixedWidthInteger.wide

  require(bitWidth > 0)

  /** The words in the instance's representation, from least to most significant. */
  def words: IndexedSeq[Long] =
    _words.to(ArraySeq)

  /** Returns `-1` if `this` is negative, `1` if `this` is positive, or `0` otherwise. */
  def signum: Int = {
    val z = numberOfLeadingZeros
    if (z == 0) {
      if (isSigned) { -1 } else { 1 }
    } else {
      if (z == 64 * words.length) { 0 } else { 1 }
    }
  }

  /** Returns the number of leading zeros in `self`'s representation. */
  def numberOfLeadingZeros: Int = {
    @tailrec def loop(i: Int, r: Int): Int = {
      val n = FixedWidthInteger.i64.numberOfLeadingZeros(_words(i))
      if (n < 64) { r + n } else { loop(i - 1, r + n) }
    }
    loop(_words.length - 1, 0)
  }

  /** Returns this value as an `Int` iff it is representable in that type. */
  def asInt: Option[Int] = {
    if (WideInteger.isRepresentable(this, 32, true)) {
      Some(_words(0).toInt)
    } else {
      None
    }
  }

  /** Returns this value as a `Long` iff it is representable in that type. */
  def asLong: Option[Long] = {
    if (WideInteger.isRepresentable(this, 64, true)) {
      Some(_words(0))
    } else {
      None
    }
  }

  /** Returns this value as a `BitInt`. */
  def toBigInt: BigInt = {
    val bytes = Array.fill(8 * _words.length)(0.toByte)
    for (i <- 0 until _words.length) {
      for (j <- 0 to 7) {
        val s = _words(_words.length - i - 1)
        val b = (j - 7) * 8
        bytes(i * 8 + j) = (s >>> b).toByte
      }
    }
    BigInt(bytes)
  }

  override def equals(other: Any): Boolean = {
    val rhs = other match {
      case r: Int =>
        WideInteger.converting(r, bitWidth, isSigned)(FixedWidthInteger.i32)
      case r: Long =>
        WideInteger.converting(r, bitWidth, isSigned)(FixedWidthInteger.i64)
      case r: WideInteger =>
        WideInteger.converting(r, bitWidth, isSigned)(FixedWidthInteger.wide)
    }
    rhs.map((r) => _words.sameElements(r._words)).getOrElse(false)
  }

  override def hashCode(): Int = {
    var result = 31
    for (i <- 0 until _words.length) {
      val s = _words(i)
      result = s.toInt ^ (s >>> 0xffffffffL).toInt ^ result
    }
    result
  }

  override def toString(): String = {
    val q = if (isSigned) { "i" } else { "u" }
    s"${q}${bitWidth} ${toBigInt}"
  }

}

object WideInteger {

  import FixedWidthInteger.WORD_WIDTH

  /** Creates an instance truncating or sign-extending `source` to fit `bitWidth`.
   *
   *  @param source The source bits used in the result
   *  @param bitWidth The number of significant bits in the result.
   *  @param signed `true` iff the result is has a signed representation.
   */
  def truncatingOrExtending[T](
      source: T, bitWidth: Int, signed: Boolean
  )(implicit w: FixedWidthInteger[T]): WideInteger = {
    // Initialize the words of the instance being created.
    val wordCount = (bitWidth + WORD_WIDTH - 1) / WORD_WIDTH
    val words = Array.fill(wordCount)(0L)
    val s = w.words(source)
    s.copyToArray(words, 0, wordCount)

    // Sign-extend the representation if necessary.
    if ((w.bitWidth(source) < bitWidth) && w.isSigned(source) && (s.last < 0)) {
      Arrays.fill(words, s.length, wordCount, -1)
    }

    new WideInteger(words, bitWidth, signed)
  }

  /** Returns an instance having the value of `source` iff it is representable as a binary integer
   *  with given properties.
   */
  def converting[T](
      source: T, bitWidth: Int, signed: Boolean
  )(implicit w: FixedWidthInteger[T]): Option[WideInteger] = {
    val wordCount = (bitWidth + WORD_WIDTH - 1) / WORD_WIDTH
    val words = Array.fill(wordCount)(0L)

    if (w.signum(source) == 0) {
      // Fast path: source is 0.
      Some(new WideInteger(words, bitWidth, signed))
    } else {
      // Slow path: copy significant bits and make sure no information is lost.
      if (!isRepresentable(source, bitWidth, signed)) {
        val q = if (signed) { "" } else { "un" }
        val t = s"${bitWidth}-bit ${q}signed integer"
        throw new IllegalArgumentException(s"'${source}' is not representable as a ${t}")
      } else {
        val s = w.words(source)
        s.copyToArray(words)
        Some(new WideInteger(words, bitWidth, signed))
      }
    }
  }

  /** Returns `true` iff `source` is representable as a binary integer with given properties. */
  def isRepresentable[T](
      source: T, bitWidth: Int, signed: Boolean
  )(implicit w: FixedWidthInteger[T]): Boolean = {
    if (w.signum(source) == 0) {
      // Trivial if the value to represent is 0.
      true
    } else if (w.bitWidth(source) <= bitWidth) {
      // Trivial if the source has a smaller representation.
      (w.numberOfLeadingZeros(source) > 0) ||
        ((w.signum(source) < 0) && signed) ||
        ((w.signum(source) > 0) && ((w.bitWidth(source) < bitWidth) || !signed))
    } else {
      // Otherwise, make sure thrown bits carry no information.
      val wordCount = (bitWidth + WORD_WIDTH - 1) / WORD_WIDTH
      val additionalBits = bitWidth % WORD_WIDTH
      val s = w.words(source)

      if (w.signum(source) > 0) {
        // All thrown bits must be zeros.
        s.drop(wordCount).forall(_ == 0L) && (if (additionalBits == 0) {
          signed && (s(wordCount - 1) >= 0)
        } else {
          (s(wordCount - 1) >>> (additionalBits - 1)) == 0L
        })
      } else if (signed) {
        // All thrown bits must be ones.
        s.drop(wordCount).forall(_ == -1L) && (if (additionalBits == 0) {
          s(wordCount - 1) < 0
        } else {
          (~s(wordCount - 1) >>> (additionalBits - 1)) == 0L
        })
      } else {
        false
      }
    }
  }

}
