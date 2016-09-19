package controllers

import configuration.ApplicationContext
import play.api.libs.json.Json
import play.api.mvc.{Action, BodyParsers, Controller}
import repository.OfferRepo
import service.{LoanNotFound, NotOffersFound, OfferCalculator}

import scala.concurrent.ExecutionContext.Implicits.global
import scala.concurrent.Future

trait OfferController extends Controller {

  case class OfferRequest(loanId: String, amount: BigDecimal, apr: Double)

  object OfferRequest {
    implicit val loanReader = Json.reads[OfferRequest]
  }

  val offerRepo: OfferRepo
  val offerCalculator: OfferCalculator

  def makeOffer() = Action.async(BodyParsers.parse.json) { request =>
    request.body.validate[OfferRequest].fold(
      errors => Future.successful(BadRequest),
      req => {
        offerRepo.createOffer(req.loanId, req.amount, req.apr) map {
          offer => Ok(s"""{"offerId":"${offer.id}"}""")
        } recover {
          case _ => InternalServerError
        }
      }
    )
  }

  def getOffer(loanId: String) = Action.async {
    offerCalculator.calculateOffer(loanId) map {
      case Right(co) => Ok(Json.toJson(co))
      case Left(LoanNotFound) => NotFound
      case Left(NotOffersFound) => UnprocessableEntity
    } recover {
      case _ => InternalServerError
    }
  }

}

object OfferController extends OfferController {
  override val offerRepo: OfferRepo = ApplicationContext.offerRepository
  override val offerCalculator: OfferCalculator = ApplicationContext.offerCalculator
}