package tech.pragmarad.tcom.client

/**
 * Component constants
 * @author pragmarad
 * @since 2020-03-09
 */
object Constants {

  /**
   * Default values
   */
  object Default {
    val MESSAGE_CONTENT = "msg"
    val FREQUENCY_MSECS = "1000"
    val MAX_BURST_COUNT = "10"
    val ACTOR_SYSTEM_NAME = "TcpAkkaStreamsActorSys"
  }

  object PropNames {
    private val PREFIX = "tcom.cli.akka."

    val ACTOR_SYSTEM_NAME: String = PREFIX + "actor-system-name"

    // Server params
    val HOST: String = PREFIX + "host"
    val PORT: String = PREFIX + "port"

    // Message params
    val MESSAGE: String = PREFIX + "message"
    val FREQUENCY_MSECS: String = PREFIX + "frequencymsecs"
    val MAX_BURST_COUNT: String = PREFIX + "maxburstcount"
  }


}
