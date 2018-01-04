package test

import io.gatling.core.Predef._
import io.gatling.core.scenario._
import io.gatling.http.Predef._

abstract class BaseTestCase extends Simulation {

  val httpProtocol = http
    .baseURLs("http://consfm12:7001", "http://consfm12:7002")
    .inferHtmlResources()
}

