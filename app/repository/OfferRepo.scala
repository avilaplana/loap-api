package repository

import domain.Offer

import scala.collection.mutable.Map
import scala.concurrent.ExecutionContext.Implicits.global
import scala.concurrent.Future

class OfferRepo(private var repo: Map[String, Seq[Offer]] = Map.empty[String, Seq[Offer]]) {

  def createOffer(loanId: String, amount: BigDecimal, apr: Double): Future[Offer] = Future {
    val newOffer = Offer(loanId, amount, apr)
    repo.get(loanId) match {
      case Some(offers) => repo = repo += (loanId -> (offers :+ newOffer))
      case None => repo = repo += (loanId -> Seq(newOffer))
    }
    newOffer
  }

  def findOffers(loanId: String): Future[Seq[Offer]] = Future {
    repo.get(loanId).getOrElse(Seq.empty[Offer])
  }
}