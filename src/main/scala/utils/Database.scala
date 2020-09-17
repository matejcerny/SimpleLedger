package utils

import cats.effect._
import common.Configuration.DatabaseConfig
import common.Domain.{Amount, BusinessTime, FullName, Id}
import doobie._
import doobie.implicits._
import doobie.implicits.javatime.JavaTimeLocalDateTimeMeta
import doobie.util.ExecutionContexts
import doobie.util.transactor.Transactor.Aux
import utils.Database.TransactionData

class Database(xa: Aux[IO, Unit]) {

  def insert(transactionData: TransactionData): IO[Int] =
    (fr"INSERT INTO simple_ledger.tb_data(sender, receiver, amount, currency, business_time)" ++
      fr"  VALUES(" ++
      fr"  ${transactionData.sender.value}" ++
      fr", ${transactionData.receiver.value}" ++
      fr", ${transactionData.amount.value}" ++
      fr", ${transactionData.amount.currency.symbol}" ++
      fr", ${transactionData.businessTime.value}" ++
      fr")").update.run.transact(xa)

  def getPersonFullName(personId: Id): IO[List[String]] =
    sql"SELECT fullname FROM simple_ledger.tb_user WHERE(id=${personId.value})"
      .query[String]
      .to[List]
      .transact(xa)

}

object Database {

  case class TransactionData(
    sender: FullName,
    receiver: FullName,
    amount: Amount,
    businessTime: BusinessTime
  )

  def apply(databaseConfig: DatabaseConfig): Database = {
    implicit val cs: ContextShift[IO] = IO.contextShift(ExecutionContexts.synchronous)

    new Database(
      Transactor.fromDriverManager[IO](
        databaseConfig.driver,
        databaseConfig.connectionString,
        databaseConfig.username,
        databaseConfig.password
      )
    )
  }

}
