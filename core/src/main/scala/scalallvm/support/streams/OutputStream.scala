package scalallvm
package support.streams

import scala.collection.immutable.ArraySeq

/** A stream that LLVM can use to write data in bulk. */
trait OutputStream extends LLVMObject with Disposable

/** An `OutputStream` that supports a "pwrite" operation. */
trait PWriteStream extends OutputStream

/** A stream that LLVM can use to write to memory. */
final class BufferOutputStream private[streams] (
    val handle: LLVM.Handle
) extends OutputStream {

  /** Returns the contents of the buffer as an array of bytes. */
  def contents: ArraySeq[Byte] =
    LLVM.BufferOutputStreamGetContents(handle).to(ArraySeq)

  def dispose(): Unit =
    LLVM.BufferOutputStreamDispose(handle)

}

/** A stream that LLVM can use to write data to a file. */
final class FileOutputStream private[streams] (val handle: LLVM.Handle) extends PWriteStream {

  def dispose(): Unit =
    LLVM.FileOutputStreamDispose(handle)

}

object OutputStream {

  def withBuffer[R](action: BufferOutputStream => R): R = {
    val instance = new BufferOutputStream(LLVM.BufferOutputStreamCreate())
    try action(instance) finally instance.dispose()
  }

  /** Returns the result of calling `action` with a new stream writing to `filename`.
   *
   *  The argument to `action` is only valid for the duration of action's `call`. It is undefined
   *  behavior to let it escape in any way.
   */
  def withFile[R](filename: String)(action: FileOutputStream => R): R = {
    val h = LLVM.FileOutputStreamCreate(filename)
    if ((h & 1L) == 0) {
      val instance = new FileOutputStream(h)
      try action(instance) finally instance.dispose()
    } else {
      throw new RuntimeException(LLVM.ConsumeError(h))
    }
  }

}
