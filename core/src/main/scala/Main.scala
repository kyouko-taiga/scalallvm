package scalallvm

object Main {

  def main(args: Array[String]): Unit = {
    val c = new Context()
    val m = new Module("test", c)

    println(m.description)

    m.dispose()
    c.dispose()
  }

}
