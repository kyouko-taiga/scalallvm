package scalallvm
package types

/** The LLVM identifier of a type. */
final class TypeKind(val rawValue: Byte) extends AnyVal

object TypeKind {

  lazy val array = new TypeKind(LLVM.ArrayTypeKind())

  lazy val double = new TypeKind(LLVM.DoubleTypeKind())

  lazy val float = new TypeKind(LLVM.FloatTypeKind())

  lazy val function = new TypeKind(LLVM.FunctionTypeKind())

  lazy val integer = new TypeKind(LLVM.IntegerTypeKind())

  lazy val pointer = new TypeKind(LLVM.PointerTypeKind())

  lazy val struct = new TypeKind(LLVM.StructTypeKind())

  lazy val void = new TypeKind(LLVM.VoidTypeKind())

}
