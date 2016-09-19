package controller

import org.scalatest.MustMatchers
import org.scalatestplus.play.PlaySpec
import play.api.libs.json.Json
import play.api.test.Helpers._
import play.api.test.{FakeApplication, FakeRequest, WithApplication}

class OfferControllerSpec extends PlaySpec with MustMatchers {


  "makeOffer" should {
    "return Bad Request when the json body is not correct" in new WithApplication(FakeApplication()) {
      val offerRequestJson1 = Json.parse("""{"loanId":"1"}""")
      val Some(offerResponse1) = route(FakeRequest(POST, "/offer").withJsonBody(offerRequestJson1))
      status(offerResponse1) mustBe (BAD_REQUEST)
    }
  }

  "getOffer" should {
    "return Not Found when there is no Loan with id 1" in new WithApplication(FakeApplication()) {
      val Some(offer) = route(FakeRequest(GET, "/offer/1"))
      status(offer) mustBe (NOT_FOUND)
    }

    "return Not Found when there is Loan but there are not offers" in new WithApplication(FakeApplication()) {

      val loanRequestJson = Json.parse("""{"amount":1000,"daysOfDuration":100}""")
      val Some(loanResponse) = route(FakeRequest(POST, "/loan").withJsonBody(loanRequestJson))
      status(loanResponse) mustBe (OK)
      val loandId: String = (contentAsJson(loanResponse) \ "loanId").as[String]

      val Some(offer) = route(FakeRequest(GET, s"/offer/$loandId"))
      status(offer) mustBe (UNPROCESSABLE_ENTITY)
    }
  }

}
