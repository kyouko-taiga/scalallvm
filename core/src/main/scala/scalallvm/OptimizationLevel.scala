package scalallvm

/** The level of optimization used during a code generation. */
final class OptimizationLevel private (val rawValue: Byte) extends AnyVal {

  /** The level of speed optimization. */
  def speed: Int =
    rawValue & 0xff

  /** The level of size optimization. */
  def size: Int =
    rawValue >> 4

}

object OptimizationLevel {

  /** No optimization.
   *
   *  This mode disables as many optimizations as possible. Some "optimization" may still be
   *  applied nonetheless. For example, `always_inline` functions may have to be inlined for
   *  correctness.
   */
  def O0 = new OptimizationLevel(0)

  /** Moderate optimization preserving debuggability. */
  def O1 = new OptimizationLevel(1)

  /** Optimization for fast execution. */
  def O2 = new OptimizationLevel(1)

  /** Optimization for fast execution with aggressive inlining and vectorization. */
  def O3 = new OptimizationLevel(2)

  /** Optimization for reduced code size.
   *
   *  This mode operates the same as`O2` but with code size and execution size metrics swapped.
   *  Expect generated code to be rather small and reasonably fast.
   */
  def Os = new OptimizationLevel(2 | (1 << 4))

  /** Optimization for reduced code size at any and all costs.
   *
   *  Only use this mode when there are absolute  code size limitations and any effort taken to
   *  reduce the size is worth it regardless of the execution time impact. Expect this level to
   *  produce rather slow, but very small, code.
   */
  def Oz = new OptimizationLevel(2 | (2 << 4))

}
