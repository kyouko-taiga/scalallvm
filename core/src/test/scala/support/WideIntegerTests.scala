import scalallvm.support.WideInteger
import scalallvm.support.FixedWidthInteger.{i32, u32, i64, wide}

class WideIntegerTests extends munit.FunSuite {

  test("isRepresentable") {
    // [i32 | u32] to i8
    assert(WideInteger.isRepresentable(0, 8, true)(i32))
    for (k <- 0 to 31; j <- 0 to k) {
      val n = (1 << k | 1 << j)
      assert(WideInteger.isRepresentable(n, 8, true)(i32) == (k < 7))
      assert(WideInteger.isRepresentable(n, 8, true)(u32) == (k < 7))
    }

    // i32 to i16 negative
    for (k <- 0 to 31) {
      val n = ~(1 << k)
      assert(WideInteger.isRepresentable(n, 16, true)(i32) == (k < 15))
    }

    // [i32 | u32] to i32
    for (k <- 0 to 31; j <- 0 to k) {
      val n = (1 << k | 1 << j)
      assert(WideInteger.isRepresentable(n, 32, true)(i32))
      assert(WideInteger.isRepresentable(n, 32, true)(u32) == (k < 31))
    }

    // [i32 | u32] to i64
    for (k <- 0 to 31; j <- 0 to k) {
      val n = (1 << k | 1 << j)
      assert(WideInteger.isRepresentable(n, 64, true)(i32))
      assert(WideInteger.isRepresentable(n, 64, true)(u32))
    }

    // [i128 | u128] to i64
    for (k <- 0 to 127; j <- 0 to k) {
      val (s, u) = makeInt128(k, j)
      assert(WideInteger.isRepresentable(s, 64, true)(wide) == (k < 63))
      assert(WideInteger.isRepresentable(u, 64, true)(wide) == (k < 63))
    }

    // i128 to i64 negative
    for (k <- 0 to 63) {
      val m = new WideInteger(Array(~(1L << k), -1L), 128, true)
      assert(WideInteger.isRepresentable(m, 64, true)(wide) == (k < 63))
    }

    // [i128 | u128] to i128
    for (k <- 0 to 127; j <- 0 to k) {
      val (s, u) = makeInt128(k, j)
      assert(WideInteger.isRepresentable(s, 128, true)(wide))
      assert(WideInteger.isRepresentable(u, 128, true)(wide) == (k < 127))
    }
  }

  test("asInt") {
    val n0 = new WideInteger(Array(2L, 0), 128, true)
    assertEquals(n0.asInt, Some(2))
    val n1 = new WideInteger(Array(-2L, -1L), 128, true)
    assertEquals(n1.asInt, Some(-2))
    val n2 = new WideInteger(Array(-2L, 0), 128, true)
    assertEquals(n2.asInt, None)
  }

  test("asLong") {
    val n0 = new WideInteger(Array(2L, 0), 128, true)
    assertEquals(n0.asLong, Some(2L))
    val n1 = new WideInteger(Array(-2L, -1L), 128, true)
    assertEquals(n1.asLong, Some(-2L))
    val n2 = new WideInteger(Array(-2L, 0), 128, true)
    assertEquals(n2.asLong, None)
  }

  test("toBigInt") {
    val n0 = new WideInteger(Array(2L, 0), 128, true)
    assertEquals(n0.toBigInt, BigInt(2))
    val n1 = new WideInteger(Array(-2L, -1L), 128, true)
    assertEquals(n1.toBigInt, BigInt(-2))
    val n2 = new WideInteger(Array(-2L, 0), 128, true)
    assertEquals(n2.toBigInt, BigInt("18446744073709551614"))
    val n3 = new WideInteger(Array(-1L, -1L), 128, true)
    assertEquals(n3.toBigInt, BigInt(-1))
  }

  test("equals") {
    val n0 = new WideInteger(Array(2L, 0), 128, true)
    assert(n0 == 2)
    assert(n0 == 2L)
    assert(n0 == new WideInteger(Array(2L, 0), 128, true))
  }

  test("hashCode") {
    val n0 = new WideInteger(Array(2L, 0), 128, true)
    val n1 = new WideInteger(Array(2L, 0), 128, true)
    assertEquals(n0.hashCode(), n1.hashCode())
  }

  /** Returns a signed and an unsigned 128-integer represented as `1 << k | 1 << l`. */
  private def makeInt128(k: Int, j: Int): (WideInteger, WideInteger) = {
    val w = if (k < 64) {
      Array(1L << k | 1L << j, 0L)
    } else {
      if (j < 64) Array(1L << j, 1L << k) else Array(0, 1L << k | 1L << j)
    }
    (new WideInteger(w, 128, true), new WideInteger(w, 128, false))
  }

}
