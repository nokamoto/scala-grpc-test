package client

import java.util.concurrent.TimeUnit

import io.grpc.netty.NettyChannelBuilder
import proto.ping.{Ping, PingServiceGrpc, Pong}

import scala.concurrent.Future
import scala.concurrent.duration.FiniteDuration

case class Client(host: String, port: Int) {
  private[this] val channel = NettyChannelBuilder.forAddress(host, port).usePlaintext(true).build()

  private[this] val stub = PingServiceGrpc.stub(channel)

  def ping(): Future[Pong] = stub.ping(Ping())

  def shutdown(timeout: FiniteDuration): Unit = {
    channel.shutdown()
    if (!channel.awaitTermination(timeout.toMillis, TimeUnit.MILLISECONDS)) {
      channel.shutdownNow()
      if (!channel.awaitTermination(timeout.toMillis, TimeUnit.MILLISECONDS)) {
        println(s"$this did not terminate.")
      }
    }
  }
}
