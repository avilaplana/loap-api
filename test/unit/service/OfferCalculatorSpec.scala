package service

import domain.Loan
import org.scalatest.concurrent.ScalaFutures
import org.scalatest.{MustMatchers, WordSpec}
import repository._

import scala.collection.mutable

class OfferCalculatorSpec extends WordSpec with MustMatchers with ScalaFutures {


  "calculateOffer" should {

    "return offer NotOffersFound when the loan with id 1 does not have offers" in {

      val service = new OfferCalculator {
        override private[service] val loanRepo: LoanRepo = new LoanRepo(mutable.Map("1" -> Loan(BigDecimal(1000), 100, "1")))
        override private[service] val offerRepo: OfferRepo = new OfferRepo()
      }

      service.calculateOffer("1").futureValue mustBe Left(NotOffersFound)
    }

    "return LoanNotFound when there is not Loan with the id 1" in {
      val service = new OfferCalculator {
        override private[service] val loanRepo: LoanRepo = new LoanRepo()
        override private[service] val offerRepo: OfferRepo = new OfferRepo()
      }

      service.calculateOffer("1").futureValue mustBe Left(LoanNotFound)
    }
  }

}
