package domain

import java.util.UUID

import play.api.libs.json.Json

case class Offer(loanId: String, amount: BigDecimal, apr: Double, id: String = UUID.randomUUID().toString)

case class CurrentOffer(amount: BigDecimal, apr: Double)

object CurrentOffer {
  implicit val currentOfferReader = Json.writes[CurrentOffer]
}

case class Loan(amount: BigDecimal, daysOfDuration: Int, id: String = UUID.randomUUID().toString)
