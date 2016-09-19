package controller

import org.scalatest.MustMatchers
import org.scalatestplus.play.PlaySpec
import play.api.libs.json.Json
import play.api.test.Helpers._
import play.api.test.{FakeApplication, FakeRequest, WithApplication}

class LoanControllerSpec extends PlaySpec with MustMatchers {

  "loan" should {
    "return Bad Request when the json body is not correct" in new WithApplication(FakeApplication()) {
      val loanRequestJson = Json.parse("""{"amount":1000}""")
      val Some(loanResponse) = route(FakeRequest(POST, "/loan").withJsonBody(loanRequestJson))
      status(loanResponse) mustBe (BAD_REQUEST)
    }
  }
}
