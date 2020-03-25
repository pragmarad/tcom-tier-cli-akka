package tech.pragmarad.tcom.client

import akka.actor._
import akka.stream.ThrottleMode
import akka.stream.scaladsl._
import akka.util.ByteString
import org.slf4j.LoggerFactory
import scala.concurrent.ExecutionContext.Implicits.global
import scala.concurrent.Future
import scala.concurrent.duration.FiniteDuration

/**
 * Starts sending messages to a TCP server and wait for response (all is as akka stream via TCP).
 * Credits: Ideas of organizing stream flow below are from EDX course "Reactive Scala".
 *
 * @param host TCP server host (DNS name or IP)
 * @param port TCP server port
 * @param actorSysName
 * @author pragmarad, reactive_scala_edx_course_team
 * @since 2020-03-09
 */
class TcpAkkaStreamClient(host: String, port: Int, actorSysName: String) {
  implicit val system: ActorSystem = ActorSystem(actorSysName)
  private val logger = LoggerFactory.getLogger(this.getClass.getName)

  // Adding TCP support:
  // - Flow:
  private val clientFlow: Flow[ByteString, ByteString, Future[Tcp.OutgoingConnection]] = Tcp(system).outgoingConnection(host, port)

  // Let's show some initial info:
  clientFlow.mapMaterializedValue(_.map {
    // TODO: still not shown in log for some reason..
    connection => logger.info("Connection established, local addr is '{}', remote addr is '{}'.", connection.localAddress,connection.remoteAddress)
  })


  /**
   * Starts sending message to remote server.
   * NOTE: Use OS specific process termination command (Ctr-C etc)
   * @param message Which kind of message to send
   * @param frequency How often to send {{message}}
   * @param maxBurstCount How many time to send message at once
   */
  def start(message: String, frequency: FiniteDuration, maxBurstCount: Int): Unit = {
    val messageBytes = ByteString(message)
    // - Source:
    val localDataSource = Source.repeat(messageBytes).throttle(1, per = frequency
      , maximumBurst = maxBurstCount, mode = ThrottleMode.Shaping)

    // - Sink:
    val localDataSink = Sink.foreach[ByteString](
          data =>  logger.info("For message '{}' received response: '{}'.", message, data))

    // - Run flow:
    localDataSource.via(clientFlow).to(localDataSink).run
  }

}

/**
 * TCP client companion object.
 */
object TcpAkkaStreamClient {
  /**
   * Factory method for creating new instance of {{TcpAkkaStreamClient}}.
   * @param host
   * @param port
   * @param actorSysName
   * @return TCP client instance
   */
  def getClient(host: String, port: Int, actorSysName: String): TcpAkkaStreamClient = {
    new TcpAkkaStreamClient(host, port, actorSysName)
  }
}

