package persistence

import cats.effect._
import common.Configuration.DatabaseConfig
import doobie._
import doobie.implicits._
import doobie.implicits.javatime.JavaTimeLocalDateTimeMeta
import doobie.util.ExecutionContexts
import doobie.util.transactor.Transactor.Aux
import persistence.Domain.Persistence

class PersistenceAPI(xa: Aux[IO, Unit]) {

  def insert(persistence: Persistence): IO[Int] =
    (fr"INSERT INTO simple_ledger.tb_data(sender, receiver, amount, currency, business_time)" ++
      fr"  VALUES(" ++
      fr"  ${persistence.sender.asString}" ++
      fr", ${persistence.receiver.asString}" ++
      fr", ${persistence.amount}" ++
      fr", ${persistence.currency.symbol}" ++
      fr", ${persistence.businessTime.value}" ++
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
