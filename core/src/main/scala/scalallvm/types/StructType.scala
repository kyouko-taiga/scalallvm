package scalallvm
package types

import support.ContainerView

/** The type of a struct in LLVM IR.
 *
 *  @param members The types of the members of the struct.
 */
final class StructType private (val handle: LLVM.Handle) extends Type {

  /** The members of this struct. */
  def members: StructType.Members =
    new StructType.Members(this)

  /** The name of this struct if it is nominal. Otherwise, `None`. */
  def name: Option[String] = {
    val n = LLVM.StructTypeGetName(handle)
    if (n == null) { None } else { Some(n) }
  }

  /** `true` iff `this` denotes a literal type. */
  def isLiteral: Boolean =
    LLVM.StructTypeIsLiteral(handle)

  /** `true` iff the fields of the struct are packed. */
  def isPacked: Boolean =
    LLVM.StructTypeIsPacked(handle)

  /** Returns a constant whose LLVM IR type is `this` that aggregates `cs`. */
  def apply(cs: Seq[values.Constant]): values.Struct =
    new values.Struct(this, cs)

  override def kind: TypeKind =
    TypeKind.struct

}

object StructType {

  def apply(t: Type): Option[StructType] =
    if (t.kind == TypeKind.struct) { Some(new StructType(t.handle)) } else { None }

  /** Creates and returns the type of a struct identified by `name` in `context` and having
   *  `members`, which are packed iff `packed` is `true`.
   *
   *  This method always creates a new type. If `name` already identifies a type in `context`, a
   *  fresh name is created using `name` as a prefix.
   *
   *  @param name The name of the returned type, which shall not be empty.
   *  @param members The types of the members in the struct.
   *  @param packed `true` iff the representation of the struct is packed.
   *  @param context The context in which the type is created.
   */
  def nominal(
      name: String, members: Seq[Type], packed: Boolean = false
  )(implicit context: Context): StructType = {
    require(!name.isEmpty, "nominal struct must have a non-empty name")
    val h = LLVM.StructTypeCreateNominalInContext(
      name, members.map(_.handle).toArray, packed, context.handle)
    new StructType(h)
  }

  /** Returns the type of a struct structurally identified in `context` by its `members`, which
   *  are packed iff `packed` is true.
   *
   *  @param members The types of the members in the struct.
   *  @param packed `true` iff the representation of the struct is packed.
   *  @param context The context in which the type is created.
   */
  def structural(
      members: Seq[Type], packed: Boolean = false
  )(implicit context: Context): StructType =  {
    val h = LLVM.StructTypeCreateStructuralInContext(
      members.map(_.handle).toArray, packed, context.handle)
    new StructType(h)
  }

  /** A view on the members of a struct type.
   *
   *  @param base The struct type containing the members in `this`.
   */
  final class Members(val base: StructType) extends ContainerView[Type, Int] {

    val startPosition: Int = 0

    val endPosition: Int =
      LLVM.StructTypeMemberCount(base.handle)

    def positionAfter(p: Int): Int =
      p + 1

    def elementAt(p: Int): Type = {
      require((startPosition <= p) && (p < endPosition))
      new Type { val handle = LLVM.StructTypeMemberAt(base.handle, p) }
    }

  }

}
