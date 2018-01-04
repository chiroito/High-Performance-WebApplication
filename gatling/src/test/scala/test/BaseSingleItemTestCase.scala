package test

import io.gatling.core.Predef._
import io.gatling.http.Predef._

import scala.concurrent.duration._
import scala.util.Random

abstract class BaseSingleItemTestCase(val url: String, val maxLoad: Int, val period: Int) extends BaseTestCase {

  val scn = scenario("SingleItem")
    .exec({
      _.setAll(("userId", Random.nextInt(10000)))
    })
    .exec(http("LopsidedRequest")
      .get(this.url)
      .queryParam("quantity", 1)
      .queryParam("userId", "${userId}")
      .queryParam("productNo", 1))
    .inject(rampUsersPerSec(0) to (this.maxLoad) during (this.period seconds))

  setUp(scn).protocols(httpProtocol)
}
