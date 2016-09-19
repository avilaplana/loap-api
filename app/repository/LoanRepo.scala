package repository

import domain.Loan

import scala.collection.mutable.Map
import scala.concurrent.ExecutionContext.Implicits.global
import scala.concurrent.Future

class LoanRepo(private var repo: Map[String, Loan] = Map.empty[String, Loan]) {

  def findLoan(id: String): Future[Option[Loan]] = Future {
    repo.get(id)
  }

  def createLoan(amount: BigDecimal, daysOfDuration: Int): Future[Loan] = {
    Future {
      val loan = Loan(amount, daysOfDuration)
      repo = repo += (loan.id -> loan)
      loan
    }
  }
}