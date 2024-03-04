package scalallvm

import scalallvm.CodeGenerationKind
import scalallvm.types.FunctionType
import scalallvm.support.streams.OutputStream

import java.nio.charset.StandardCharsets

object Main {

  def main(args: Array[String]): Unit = {
    Context.withNew { (llvm) => emitModule()(llvm) }
  }

  private def emitModule()(implicit llvm: Context): Unit = {
    llvm.withNewModule("m") { (m) =>
      val f = m.declareFunction("main", new FunctionType(Nil, llvm.i32))
      val e = f.appendBasicBlock("entry")

      llvm.withNewBuilder { (b) =>
        b.positionAtEndOf(e)
        b.rturn(llvm.i32(42))
      }

      println(m.description)
      TargetMachine.withNew() { (tm) =>
        // OutputStream.withFile("/tmp/a.out") { (os) =>
        //   tm.emit(m, os)
        // }
        OutputStream.withBuffer { (os) =>
          tm.emit(m, CodeGenerationKind.assembly, os)
          val s = new String(os.contents.toArray, StandardCharsets.UTF_8)
          println(s)
        }
      }
    }
  }

}
