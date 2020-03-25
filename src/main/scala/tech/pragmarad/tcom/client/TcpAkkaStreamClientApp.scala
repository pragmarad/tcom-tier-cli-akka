package tech.pragmarad.tcom.client

import java.util.concurrent.TimeUnit

import cats.implicits._
import com.monovore.decline.{Command, Opts}
import com.typesafe.config.Config
import org.slf4j.LoggerFactory
import tech.pragmarad.tcom.commons.{ConfigCommonConstants, HoconConfigUtil, StringUtil}

import scala.concurrent.duration.FiniteDuration
import scala.util.Try

/**
 * Tcp client app starter.<br>
 * This app accepts arguments: srvhost,srvport,message,frequencymsecs,maxburstcount
 */
object TcpAkkaStreamClientApp {
  private val logger = LoggerFactory.getLogger(this.getClass.getName)
  private val cfg: Config = HoconConfigUtil.getConfig()

  private val appOptions = buildAppOptions()

  private val appCommand = Command(
    name = "TcpAkkaStreamClientApp",
    header = "Launch sending messages via TCP as an akka stream."
  ) {
    appOptions
  }

  /**
   * Main
   *
   * @param args
   */
  def main(args: Array[String]):Unit = appCommand.parse(args, sys.env) match {
    case Left(help) => {
      System.err.println(s"Args << ${args}>> could not be parsed.\n ${help}")
      sys.exit(1)
    }
    case Right(appOptions) => {
      logger.info("TcpClient starting with options '{}'..", appOptions)

      // Extract Actor system name:
      val actorSysNameFromConf = cfg.getString(Constants.PropNames.ACTOR_SYSTEM_NAME)
      val actorSysName = StringUtil.getValueWithDefaultFallback(actorSysNameFromConf, Constants.Default.ACTOR_SYSTEM_NAME)

      // Extract options:
      val srvHost = appOptions._1
      val srvPort = appOptions._2
      val message = appOptions._3
      val frequencyMsecs: FiniteDuration = FiniteDuration(appOptions._4, TimeUnit.MILLISECONDS)
      val maxBurstCount = appOptions._5

      // Now, let's start a client:
      val tcpClient = TcpAkkaStreamClient.getClient(srvHost, srvPort, actorSysName)
      tcpClient.start(message, frequencyMsecs, maxBurstCount)

      logger.info("TcpClient started.")
    }
  }

  /**
   * TODO: consider some kind of map instead of brittle tuple
   *
   * @return Options of srvhost,srvport,message,frequencymsecs,maxburstcount
   */
  private def buildAppOptions(): Opts[(String, Int, String, Long, Int)] = {

    //------------
    // TODO: Figure out more type safe options extraction
    // Params: srvhost,srvport,message,frequencymsecs,maxburstcount

    // 1. srvhost
    val srvhostFromConf = HoconConfigUtil.getStringOption(cfg, Constants.PropNames.HOST).getOrElse(ConfigCommonConstants.Default.HOST)
    val srvhostOrDefault = Opts.option[String]("srvhost", short = "h", metavar = "string"
      , help = "Server host name or IP.").withDefault(srvhostFromConf)

    // 2. srvport
    val srvportFromConf = HoconConfigUtil.getStringOption(cfg, Constants.PropNames.PORT).getOrElse(ConfigCommonConstants.Default.PORT)
    val srvportOrDefault = Opts.option[String]("srvport", short = "p", metavar = "int"
      , help = "Server port.").withDefault(srvportFromConf)
      .validate("Port must be positive int!")({ value => Try(value.toInt).isSuccess && value.toInt > 0 }).map {
      _.toInt
    }

    // 3. message
    val messageFromConf = HoconConfigUtil.getStringOption(cfg, Constants.PropNames.MESSAGE).getOrElse(Constants.Default.MESSAGE_CONTENT)
    val messageOrDefault = Opts.option[String]("message", short = "m", metavar = "string"
      , help = "Message this client will send to a server in form of a stream.").withDefault(messageFromConf)

    // 4. frequencymsecs
    val frequencymsecsFromConf = HoconConfigUtil.getStringOption(cfg, Constants.PropNames.FREQUENCY_MSECS).getOrElse(Constants.Default.FREQUENCY_MSECS)
    val frequencymsecsOrDefault = Opts.option[String]("frequencymsecs", short = "f", metavar = "long"
      , help = "Frequency of sending messages,in millseconds.").withDefault(frequencymsecsFromConf)
      .validate("Frequency (in msecs) must be positive long!")({ value => Try(value.toLong).isSuccess && value.toLong > 0 }).map {
      _.toLong
    }

    // 5. maxburstcount
    val maxburstcountFromConf = HoconConfigUtil.getStringOption(cfg, Constants.PropNames.MAX_BURST_COUNT).getOrElse(Constants.Default.MAX_BURST_COUNT)
    val maxburstcountOrDefault = Opts.option[String]("maxburstcount", short = "m", metavar = "int"
      , help = "Max burst count of sending messages.").withDefault(maxburstcountFromConf)
      .validate("Max burst count must be positive int!")({ value => Try(value.toInt).isSuccess && value.toInt > 0 }).map {
      _.toInt
    }

    // Gather all options:
    val appOptions = (srvhostOrDefault, srvportOrDefault, messageOrDefault, frequencymsecsOrDefault, maxburstcountOrDefault).tupled
    // TODO: Consider mapN to map into some kind of Map (positional config is brittle)

    appOptions
  }

}