package server

import scala.concurrent.duration._

object Main {
  def main(args: Array[String]): Unit = {
    val port = sys.props.getOrElse("port", "9000").toInt
    val server = Server(port)

    sys.addShutdownHook(server.shutdown(10.seconds))

    println(s"$server start")
    server.start()
    server.awaitTermination()
  }
}
