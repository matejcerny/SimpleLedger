import cats.effect.{ExitCode, IO, IOApp}
import cats.implicits._
import com.typesafe.scalalogging.LazyLogging
import common.Configuration.buildAppConfig
import common.Generator
import trasaction.TransactionActor

object SimpleLedger extends IOApp with LazyLogging {

  def run(args: List[String]): IO[ExitCode] = args.headOption match {
    case Some(path) => runSuccess(path)
    case None => runError
  }

  def runError: IO[ExitCode] =
    IO(logger.error("You must specify configuration file"))
      .as(ExitCode.Error)

  def runSuccess(path: String): IO[ExitCode] =
    (
      for {
        _ <- IO(logger.info("Starting the application"))
        appConfig <- IO(buildAppConfig(path))
        actorSystem = TransactionActor.setup(appConfig)
        randomMessage = Generator.randomTransactionMessage
      } yield actorSystem ! randomMessage
    ).as(ExitCode.Success)

}
