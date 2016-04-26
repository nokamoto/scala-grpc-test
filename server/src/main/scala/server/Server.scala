package server

import java.util.concurrent.TimeUnit

import io.grpc.netty.NettyServerBuilder
import proto.ping.{Ping, PingServiceGrpc, Pong}
import proto.ping.PingServiceGrpc.PingService

import scala.concurrent.Future
import scala.concurrent.ExecutionContext.global
import scala.concurrent.duration.FiniteDuration

case class Server(port: Int) {
  private[this] val service = new PingService {
    override def ping(request: Ping): Future[Pong] = {
      println("pong: " + request)
      Future.successful(Pong())
    }
  }

  private[this] val server = Seq(PingServiceGrpc.bindService(service, global)).
    foldLeft(NettyServerBuilder.forPort(port)) { case (builder, svc) => builder.addService(svc) }.build()

  def start(): Unit = server.start()

  def awaitTermination(): Unit = server.awaitTermination()

  def shutdown(timeout: FiniteDuration): Unit = {
    server.shutdown()
    if (!server.awaitTermination(timeout.toMillis, TimeUnit.MILLISECONDS)) {
      server.shutdownNow()
      if (!server.awaitTermination(timeout.toMillis, TimeUnit.MILLISECONDS)) {
        println(s"$this did not terminate.")
      }
    }
  }
}
