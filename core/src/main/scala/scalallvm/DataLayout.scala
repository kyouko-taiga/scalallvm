package scalallvm

import types.{StructType, Type}

/** How data are represented in memory for a particular target machine. */
final class DataLayout private[scalallvm] (val handle: LLVM.Handle) extends LLVMObject {

  /** `true` iff the the data layout was constructed from an empty string. */
  def isDefault: Boolean =
    LLVM.DataLayoutIsDefault(handle)

  /** A textual description of this data layout. */
  def description: String =
    LLVM.DataLayoutDescription(handle)

  /** Returns the number of bits in the representation of `t`'s instances. */
  def bitWidth(t: Type): Int =
    LLVM.DataLayoutBitWidth(handle, t.handle)

  /** Returns the storage size of the representation of `t`'s instances in bytes. */
  def storageSize(t: Type): Int =
    LLVM.DataLayoutStorgeSize(handle, t.handle)

  /** Returns the preferred alignment of `t`'s instances in bytes. */
  def preferredAlignment(t: Type): Int =
    LLVM.DataLayoutPreferredAlignment(handle, t.handle)

  /** Returns the ABI alignment of `t`'s instances in bytes. */
  def requiredAlignment(t: Type): Int =
    LLVM.DataLayoutABIAlignment(handle, t.handle)

  /** Returns the offset in bytes of the element at index `i` in `t`.
   *
   *  @param i a valid element index in `t`.
   *  @param t a struct type.
   */
  def byteOffset(i: Int, t: StructType): Int =
    LLVM.DataLayoutOffsetOfElement(handle, i, t.handle)

  /** Returns the index of the element containing the byte at offset `i`.
   *
   *  @param i a byte offset that is withing the bounds of an instance of `t`.
   *  @param t a struct type.
   */
  def indexOfElementContaining(i: Int, t: StructType): Int =
    LLVM.DataLayoutIndexOfElementContaining(handle, i, t.handle)

  /** Returns `true` if `t`'s instances contain padding bits.*/
  def hasPadding(t: StructType): Boolean =
    LLVM.DataLayoutHasPaddingBits(handle, t.handle)

  def dispose(): Unit =
    LLVM.DataLayoutDispose(handle)

}
