import Generator._
import cats.effect.{ExitCode, IO, IOApp}
import cats.implicits._
import com.typesafe.scalalogging.LazyLogging
import common.Configuration.buildAppConfig
import common.Domain.Transaction
import persistence.PersistenceAPI

object SimpleLedger extends IOApp with LazyLogging {

  def run(args: List[String]): IO[ExitCode] = args.headOption match {
    case Some(path) => runSuccess(path)
    case None => runError
  }

  def runError: IO[ExitCode] =
    IO(logger.error("You must specify configuration file"))
      .as(ExitCode.Error)

  def runSuccess(path: String): IO[ExitCode] =
    IO {

      val appConfig = buildAppConfig(path)
      val persistenceAPI = PersistenceAPI(appConfig.databaseConfig)

      val transaction = Transaction(
        randomPerson,
        randomPerson,
        randomAmount,
        randomCurrency
      )

      (for { i <- persistenceAPI.insert(transaction) } yield { logger.info(i.toString) })
        .unsafeRunSync()

    }.as(ExitCode.Success)

}
