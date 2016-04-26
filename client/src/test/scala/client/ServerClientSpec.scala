package client

import java.net.ServerSocket

import org.scalatest.FlatSpec
import server.Server

import scala.concurrent.Await
import scala.concurrent.duration._

class ServerClientSpec extends FlatSpec {
  import ServerClientSpec._

  it should "reach a grpc server" in {
    test { client =>
      Await.result(client.ping(), 3.seconds)
    }
  }
}

object ServerClientSpec {
  private[this] def availablePort(): Int = {
    val socket = new ServerSocket(0)
    try {
      socket.getLocalPort
    } finally {
      socket.close()
    }
  }

  def test(f: Client => Unit): Unit = {
    val port = availablePort()
    val server = Server(port)
    val client = Client("localhost", port)
    try {
      server.start()
      f(client)
    } finally {
      client.shutdown(10.seconds)
      server.shutdown(10.seconds)
    }
  }
}
