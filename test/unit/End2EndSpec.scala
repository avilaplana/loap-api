import org.scalatest.{FunSpec, GivenWhenThen, MustMatchers}
import org.scalatestplus.play.PlaySpec
import play.api.libs.json.Json
import play.api.test.Helpers._
import play.api.test.{FakeApplication, FakeRequest, WithApplication}


class End2EndSpec extends PlaySpec with MustMatchers with GivenWhenThen {

  "End2End" should {
    "Loan request with 2 offers" in new WithApplication(FakeApplication()) {

      Given("Loan request with amount: £1000 and duration in days: 100")
      val loanRequestJson = Json.parse("""{"amount":1000,"daysOfDuration":100}""")
      val Some(loanResponse) = route(FakeRequest(POST, "/loan").withJsonBody(loanRequestJson))
      status(loanResponse) mustBe (OK)
      val loandId: String = (contentAsJson(loanResponse) \ "loanId").as[String]

      And("Offer with amount: £100 and apr: 5%")
      val offerRequestJson1 = Json.parse(s"""{"loanId":"$loandId","amount":100,"apr":5}""")
      val Some(offerResponse1) = route(FakeRequest(POST, "/offer").withJsonBody(offerRequestJson1))
      status(offerResponse1) mustBe (OK)

      And("Offer with amount: £500 and apr: 8.6%")
      val offerRequestJson2 = Json.parse(s"""{"loanId":"$loandId","amount":500,"apr":8.6}""")
      val Some(offerResponse2) = route(FakeRequest(POST, "/offer").withJsonBody(offerRequestJson2))
      status(offerResponse2) mustBe (OK)

      When(s"Request offer for the loan with id: $loandId")
      val Some(offer) = route(FakeRequest(GET, s"/offer/$loandId"))
      status(offer) mustBe (OK)

      Then("The offer with amount: £600 and apr:8%")
      (contentAsJson(offer) \ "amount").as[BigDecimal] mustBe BigDecimal(600)
      (contentAsJson(offer) \ "apr").as[Double] mustBe 8
    }

    "Loan request with 4 offers" in new WithApplication(FakeApplication()) {

      Given("Loan request with amount: £1000 and duration in days: 100")
      val loanRequestJson = Json.parse("""{"amount":1000,"daysOfDuration":100}""")
      val Some(loanResponse) = route(FakeRequest(POST, "/loan").withJsonBody(loanRequestJson))
      status(loanResponse) mustBe (OK)
      val loandId: String = (contentAsJson(loanResponse) \ "loanId").as[String]

      And("Offer with amount: £100 and apr: 5%")
      val offerRequestJson1 = Json.parse(s"""{"loanId":"$loandId","amount":100,"apr":5}""")
      val Some(offerResponse1) = route(FakeRequest(POST, "/offer").withJsonBody(offerRequestJson1))
      status(offerResponse1) mustBe (OK)

      And("Offer with amount: £600 and apr: 6%")
      val offerRequestJson2 = Json.parse(s"""{"loanId":"$loandId","amount":600,"apr":6}""")
      val Some(offerResponse2) = route(FakeRequest(POST, "/offer").withJsonBody(offerRequestJson2))
      status(offerResponse2) mustBe (OK)

      And("Offer with amount: £600 and apr: 7%")
      val offerRequestJson3 = Json.parse(s"""{"loanId":"$loandId","amount":600,"apr":7}""")
      val Some(offerResponse3) = route(FakeRequest(POST, "/offer").withJsonBody(offerRequestJson3))
      status(offerResponse3) mustBe (OK)

      And("Offer with amount: £500 and apr: 8.2%")
      val offerRequestJson4 = Json.parse(s"""{"loanId":"$loandId","amount":500,"apr":8.2}""")
      val Some(offerResponse4) = route(FakeRequest(POST, "/offer").withJsonBody(offerRequestJson4))
      status(offerResponse4) mustBe (OK)


      When(s"Request offer for the loan with id: $loandId")
      val Some(offer) = route(FakeRequest(GET, s"/offer/$loandId"))
      status(offer) mustBe (OK)

      Then("The offer with amount: £1000 and apr:6.2%")
      (contentAsJson(offer) \ "amount").as[BigDecimal] mustBe BigDecimal(1000)
      (contentAsJson(offer) \ "apr").as[Double] mustBe 6.2
    }
  }
}