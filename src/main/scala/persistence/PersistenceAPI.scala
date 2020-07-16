package persistence

import cats.effect._
import common.Configuration.DatabaseConfig
import common.Domain.Transaction
import doobie._
import doobie.implicits._
import doobie.implicits.javatime.JavaTimeLocalDateTimeMeta //used for businessTime conversion
import doobie.util.ExecutionContexts
import doobie.util.transactor.Transactor.Aux

class PersistenceAPI(xa: Aux[IO, Unit]) {

  def insert(transaction: Transaction): IO[Int] =
    (fr"INSERT INTO simple_ledger.tb_data(sender, receiver, amount, currency, business_time)" ++
      fr"  VALUES(" ++
      fr"  ${transaction.sender.asString}" ++
      fr", ${transaction.receiver.asString}" ++
      fr", ${transaction.amount}" ++
      fr", ${transaction.currency.symbol}" ++
      fr", ${transaction.businessTime.value}" ++
      fr")").update.run.transact(xa)

}

object PersistenceAPI {

  def apply(databaseConfig: DatabaseConfig): PersistenceAPI = {
    implicit val cs: ContextShift[IO] = IO.contextShift(ExecutionContexts.synchronous)

    new PersistenceAPI(
      Transactor.fromDriverManager[IO](
        databaseConfig.driver,
        databaseConfig.connectionString,
        databaseConfig.username,
        databaseConfig.password
      )
    )
  }

}
