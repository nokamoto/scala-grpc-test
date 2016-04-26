package client

import scala.concurrent.Await
import scala.concurrent.duration._

object Main {
  def main(args: Array[String]): Unit = {
    val host = sys.props.getOrElse("host", "localhost")
    val port = sys.props.getOrElse("port", "9000").toInt
    val client = Client(host, port)

    sys.addShutdownHook(client.shutdown(10.seconds))

    while (true) {
      println("ping: " + Await.result(client.ping(), 3.seconds))
      Thread.sleep(1000)
    }
  }
}
