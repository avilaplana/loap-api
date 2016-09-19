package configuration

import repository.{LoanRepo, OfferRepo}
import service.OfferCalculator

object ApplicationContext {

  val loanRepository: LoanRepo = new LoanRepo()
  val offerRepository: OfferRepo = new OfferRepo()
  val offerCalculator: OfferCalculator = new OfferCalculator {
    override val loanRepo: LoanRepo = loanRepository
    override val offerRepo: OfferRepo = offerRepository
  }
}
