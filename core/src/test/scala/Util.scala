package util

import scalallvm.{Context, InstructionBuilder, Module}
import scalallvm.types.FunctionType

object TestUtil {

  def withBuilder[R](action: (Context, InstructionBuilder) => R): R = {
    Module.withNew("m") { (llvm, m) =>
      val f = m.declareFunction("main", new FunctionType(Nil, llvm.i32))
      val e = f.appendBasicBlock("entry")

      llvm.withNewBuilder { (b) =>
        b.positionAtEndOf(e)
        action(llvm, b)
      }
    }
  }

}
