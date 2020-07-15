import Configuration.buildAppConfig
import Domain.Currency.Cardano
import Domain.Transaction
import Generator.randomPerson
import cats.effect.{ExitCode, IO, IOApp}
import cats.implicits._
import com.typesafe.scalalogging.LazyLogging

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
      val db = Database(appConfig.databaseConfig)
      val transaction = Transaction(
        randomPerson,
        randomPerson,
        Cardano
      )

      (for { i <- db.insert(transaction) } yield { logger.info(i.toString) })
        .unsafeRunSync()

    }.as(ExitCode.Success)

}
