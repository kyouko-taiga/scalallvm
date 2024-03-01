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

  // --- IRBuilder ------------------------------------------------------------

  @native def InstructionBuilderCreateInContext(context: Handle): Handle

  @native def InstructionBuilderDispose(self: Handle): Unit

  @native def InstructionBuilderGetInsertionBlock(self: Handle): Handle

  @native def InstructionBuilderMakeAlloca(
      self: Handle, tpe: Handle, space: Int, size: Handle, name: String
  ): Handle

  @native def InstructionBuilderMakeBr(self: Handle, destination: Handle): Handle

  @native def InstructionBuilderMakeConditionalBr(
      self: Handle, condition: Handle, success: Handle, failure: Handle
  ): Handle

  @native def InstructionBuilderMakeGEP(
      self: Handle, base: Handle, baseType: Handle, indices: Array[Handle],
      name: String, inBounds: Boolean
  ): Handle

  @native def InstructionBuilderMakeLoad(
      self: Handle, source: Handle, sourceType: Handle, isVolatile: Boolean, name: String
  ): Handle

  @native def InstructionBuilderMakeReturn(self: Handle, value: Handle): Handle

  @native def InstructionBuilderMakeStore(
      self: Handle, value: Handle, target: Handle, isVolatile: Boolean
  ): Handle

  @native def InstructionBuilderMakeStructGEP(
      self: Handle, base: Handle, baseType: Handle, index: Int, name: String
  ): Handle

  @native def InstructionBuilderMakeSwitch(
      self: Handle, condition: Handle, default: Handle,
      patterns: Array[Handle], destinations: Array[Handle]
  ): Handle

  @native def InstructionBuilderMakeTruncTo(
      self: Handle, source: Handle, target: Handle, name: String
  ): Handle

  @native def InstructionBuilderMakeUnreachable(self: Handle): Handle

  @native def InstructionBuilderPositionAtEndOfBlock(self: Handle, block: Handle): Unit

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

  @native def ValueGetName(self: Handle): String

  @native def ValueGetType(self: Handle): Handle

  @native def AllocaGetAddressSpace(self: Handle): Int

  @native def AllocaGetAlignment(self: Handle): Long

  @native def AllocaGetAllocatedType(self: Handle): Handle

  @native def AllocaGetAllocationCount(self: Handle): Handle

  @native def AllocaIsStatic(self: Handle): Boolean

  @native def AllocaFromValue(source: Handle): Handle

  @native def AllocaSetAlignment(self: Handle, a: Long): Unit

  @native def BasicBlockCreateInParent(name: String, next: Handle, parent: Handle): Handle

  @native def BasicBlockGetParent(self: Handle): Handle

  @native def BranchIsConditional(self: Handle): Boolean

  @native def BranchGetCondition(self: Handle): Handle

  @native def BranchFromValue(source: Handle): Handle

  @native def BranchSuccessorAt(self: Handle, position: Int): Handle

  @native def ConstantAggregateMemberAt(self: Handle, position: Int): Handle

  @native def ConstantArray(element: Handle, members: Array[Handle]): Handle

  @native def ConstantNullValue(tpe: Handle): Handle

  @native def ConstantIntCreate(tpe: Handle, value: Long, signed: Boolean): Handle

  @native def ConstantIntCreateWide(
    context: Handle, bitWidth: Int, words: Array[Long], signed: Boolean): Handle

  @native def ConstantIntGetSExtValue(tpe: Handle): Long

  @native def ConstantIntGetZExtValue(tpe: Handle): Long

  @native def ConstantDouble(tpe: Handle, value: Double): Handle

  @native def ConstantPoison(tpe: Handle): Handle

  @native def ConstantStringInContext(
      text: String, nullTerminated: Boolean, context: Handle
  ): Handle

  @native def ConstantStruct(tpe: Handle, members: Array[Handle]): Handle

  @native def ConstantUndefined(tpe: Handle): Handle

  @native def LoadGetAlignment(self: Handle): Long

  @native def LoadIsVolatile(self: Handle): Boolean

  @native def LoadFromValue(source: Handle): Handle

  @native def LoadSetAlignment(self: Handle, a: Long): Unit

  @native def StoreGetAlignment(self: Handle): Long

  @native def StoreIsVolatile(self: Handle): Boolean

  @native def StoreFromValue(source: Handle): Handle

  @native def StoreSetAlignment(self: Handle, a: Long): Unit

  @native def SwitchGetCondition(self: Handle): Handle

  @native def SwitchGetDefaultDestination(self: Handle): Handle

  @native def SwitchFromValue(source: Handle): Handle

  @native def SwitchSuccessorAt(self: Handle, position: Int): Handle

  @native def SwitchSuccessorCount(self: Handle): Int

  // --- Functions ------------------------------------------------------------

  @native def FunctionCreateInModule(
      n: String, tpe: Handle, linkage: Byte, space: Int, module: Handle
  ): Handle

  @native def FunctionBasicBlockEntry(self: Handle): Handle

  @native def FunctionBasicBlocks(self: Handle): Array[Handle]

  @native def FunctionGetByNameInModule(n: String, module: Handle): Handle

  @native def FunctionParameterAfter(self: Handle, position: Handle): Handle

  @native def FunctionParameterBegin(self: Handle): Handle

  @native def FunctionParameterCount(self: Handle): Int

  @native def FunctionParameterEnd(self: Handle): Handle

  @native def FunctionVerifiy(self: Handle): String

  @native def LinkageExternal(): Byte

  // --- Support --------------------------------------------------------------

}
