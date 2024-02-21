package scalallvm

import com.github.sbt.jni.nativeLoader

@nativeLoader("scalallvm0")
object LLVM {

  /** A handle to a resource used by LLVM. */
  type Handle = Long

  // --- Context --------------------------------------------------------------

  @native def ContextCreate(): Handle

  @native def ContextDispose(self: Handle): Unit

  // --- Module ---------------------------------------------------------------

  @native def ModuleCreateWithNameInContext(name: String, context: Handle): Handle

  @native def ModuleDescription(self: Handle): String

  @native def ModuleGetName(self: Handle): String

  @native def ModuleDispose(self: Handle): Unit

  @native def ModuleSetName(self: Handle, name: String): Unit

  // --- Types ----------------------------------------------------------------

  @native def TypeDescription(self: Handle, isForDebug: Boolean): String

  @native def TypeGetKind(self: Handle): Byte

  @native def ArrayTypeCreateInContext(count: Int, element: Handle, context: Handle): Handle

  @native def ArrayTypeCount(self: Handle): Int

  @native def ArrayTypeElement(self: Handle): Handle

  @native def ArrayTypeKind(): Byte

  @native def DoubleTypeInContext(context: Handle): Handle

  @native def DoubleTypeKind(): Byte

  @native def FloatTypeInContext(context: Handle): Handle

  @native def FloatTypeKind(): Byte

  @native def FunctionType(parameters: Array[Handle], returnType: Handle): Handle

  @native def FunctionTypeKind(): Byte

  @native def FunctionTypeParameterAt(self: Handle, position: Int): Handle

  @native def FunctionTypeParameterCount(self: Handle): Int

  @native def FunctionTypeReturn(self: Handle): Handle

  @native def IntTypeInContext(bitWidth: Int, context: Handle): Handle

  @native def IntegerTypeKind(): Byte

  @native def PointerTypeInContext(space: Int, context: Handle): Handle

  @native def PointerTypeKind(): Byte

  @native def PointerTypeGetAddressSpace(self: Handle): Int

  @native def StructTypeCreateNominalInContext(
      name: String, members: Array[Handle], packed: Boolean, context: Handle
  ): Handle

  @native def StructTypeCreateStructuralInContext(
      members: Array[Handle], packed: Boolean, context: Handle
  ): Handle

  @native def StructTypeGetName(self: Handle): String

  @native def StructTypeIsLiteral(self: Handle): Boolean

  @native def StructTypeIsOpaque(self: Handle): Boolean

  @native def StructTypeIsPacked(self: Handle): Boolean

  @native def StructTypeKind(): Byte

  @native def StructTypeMemberAt(self: Handle, position: Int): Handle

  @native def StructTypeMemberCount(self: Handle): Int

  @native def VoidTypeInContext(context: Handle): Handle

  @native def VoidTypeKind(): Byte

  // --- Values ---------------------------------------------------------------

  @native def ValueDescription(self: Handle, isForDebug: Boolean): String

  @native def ValueGetType(self: Handle): Handle

  @native def ConstantAggregateMemberAt(self: Handle, position: Int): Handle

  @native def ConstantNullValue(tpe: Handle): Handle

  @native def ConstantIntCreate(tpe: Handle, value: Long, signed: Boolean): Handle

  @native def ConstantIntCreateWide(
    context: Handle, bitWidth: Int, words: Array[Long], signed: Boolean): Handle

  @native def ConstantIntGetSExtValue(tpe: Handle): Long

  @native def ConstantIntGetZExtValue(tpe: Handle): Long

  @native def ConstantDouble(tpe: Handle, value: Double): Handle

  @native def ConstantStruct(tpe: Handle, members: Array[Handle]): Handle

  @native def ConstantUndefined(tpe: Handle): Handle

  // --- Support --------------------------------------------------------------

}
