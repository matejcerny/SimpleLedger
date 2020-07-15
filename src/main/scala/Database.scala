import Configuration.DatabaseConfig
import Domain.Transaction
import cats.effect._
import doobie._
import doobie.implicits._
import doobie.util.ExecutionContexts
import doobie.util.transactor.Transactor.Aux

class Database(xa: Aux[IO, Unit]) {

  def insert(transaction: Transaction): IO[Int] =
    (fr"INSERT INTO simple_ledger.tb_data(sender, receiver, currency, date_ts)" ++
      fr"  VALUES(" ++
      fr"  ${transaction.sender.asString}" ++
      fr", ${transaction.receiver.asString}" ++
      fr", ${transaction.currency.symbol}" ++
      fr", current_timestamp" ++
      fr")").update.run.transact(xa)

}

object Database {

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
