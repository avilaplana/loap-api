package service

import domain.{CurrentOffer, Loan, Offer}
import repository._

import scala.concurrent.ExecutionContext.Implicits.global
import scala.concurrent.Future

sealed trait Error

case object LoanNotFound extends Error

case object NotOffersFound extends Error

trait OfferCalculator {
  private[service] val loanRepo: LoanRepo
  private[service] val offerRepo: OfferRepo

  def calculateOffer(loanId: String): Future[Either[Error, CurrentOffer]] = {
    def filter(offers: Seq[Offer], loan: Loan, amountAccumulated: BigDecimal): Seq[Offer] = {
      offers match {
        case Nil => Seq.empty[Offer]
        case o +: _ if (o.amount + amountAccumulated) < loan.amount => Seq(o) ++ filter(offers.tail, loan, o.amount + amountAccumulated)
        case o +: _ => Seq(o.copy(amount = loan.amount - amountAccumulated))
      }
    }

    def calculateApr(offers: Seq[Offer], amount: BigDecimal) = offers.foldLeft(BigDecimal(0)) {
      (partial, o) => partial + (o.amount * o.apr) / amount
    }

    loanRepo.findLoan(loanId) flatMap {
      case Some(loan) =>
        offerRepo.findOffers(loanId) map {
          case Nil => Left(NotOffersFound)
          case offers =>
            val offersOrdered = offers.sortWith((o1, o2) => o1.apr < o2.apr)
            val offersFiltered= filter(offersOrdered, loan, 0)
            val amount = offersFiltered.map(_.amount).sum
            val apr = calculateApr(offersFiltered, amount)
            Right(CurrentOffer(offersFiltered.map(_.amount).sum, apr.toDouble))
        }

      case _ => Future.successful(Left(LoanNotFound))
    }
  }
}

