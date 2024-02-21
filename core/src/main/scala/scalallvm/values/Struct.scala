package scalallvm
package values

import support.ArrayView
import types.StructType

/** A constant struct value in LLVM IR. */
final class Struct private (val handle: LLVM.Handle) extends Constant {

  /** Creates an instance whose LLVM IR type is `tpe` that aggregates `members`. */
  def this(tpe: StructType, members: Seq[Constant]) =
    this(LLVM.ConstantStruct(tpe.handle, members.map(_.handle).toArray))

  /** The members of this struct. */
  def members: Struct.Members =
    new Struct.Members(this)

}

object Struct {

  /** A view on the members of a struct.
   *
   *  @param base The struct containing the members in `this`.
   */
  final class Members(val base: Struct) extends ArrayView[Constant] {

    val startPosition: Int = 0

    val endPosition: Int =
      StructType(base.tpe).get.members.endPosition

    def elementAt(p: Int): Constant = {
      require((startPosition <= p) && (p < endPosition))
      new Constant { val handle = LLVM.ConstantAggregateMemberAt(base.handle, p) }
    }

  }

}
