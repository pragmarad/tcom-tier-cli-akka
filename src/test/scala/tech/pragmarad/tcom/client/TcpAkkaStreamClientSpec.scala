package tech.pragmarad.tcom.client

import org.scalatest.flatspec.AnyFlatSpec

/**
  * Validation of tests system work.
  */
class TcpAkkaStreamClientSpec extends AnyFlatSpec {

  "Client" should "return OK status" in {
    val host = "localhost"
    val port = 15621
    val actorSysName = "TestTcpCliSys"
    val client = new TcpAkkaStreamClient(host, port, actorSysName)
    // TODO: validate this streaming guy
//    val result = TBD
//    val expected  = TBD
//    assert(expected.equals(result))
  }
}
