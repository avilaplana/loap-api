package controllers

import configuration.ApplicationContext
import play.api.libs.json.Json
import play.api.mvc.{Action, BodyParsers, Controller}
import repository.LoanRepo

import scala.concurrent.ExecutionContext.Implicits.global
import scala.concurrent.Future

trait LoanController extends Controller {

  case class LoanRequest(amount: BigDecimal, daysOfDuration: Int)

  object LoanRequest {
    implicit val loanReader = Json.reads[LoanRequest]
  }

  val loanRepo: LoanRepo

  def loan() = Action.async(BodyParsers.parse.json) { request =>
    request.body.validate[LoanRequest].fold(
      errors => Future.successful(BadRequest),
      req => {
        loanRepo.createLoan(req.amount, req.daysOfDuration) map {
          loan => Ok(s"""{"loanId":"${loan.id}"}""")
        } recover {
          case _ => InternalServerError
        }
      }
    )
  }
}

object LoanController extends LoanController {
  override val loanRepo: LoanRepo = ApplicationContext.loanRepository
}