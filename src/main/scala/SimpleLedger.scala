import Generator._
import akka.actor.typed.ActorSystem
import akka.actor.typed.scaladsl.Behaviors
import cats.effect.{ExitCode, IO, IOApp}
import cats.implicits._
import com.typesafe.scalalogging.LazyLogging
import common.Configuration.buildAppConfig
import common.Domain.TransactionMessage
import persistence.{PersistenceAPI, PersistenceActor}

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
      logger.info("Starting the application")

      val appConfig = buildAppConfig(path)

      val transactionBehavior = Behaviors.setup[TransactionMessage] { c =>
        PersistenceActor(c, PersistenceAPI(appConfig.databaseConfig))
      }

      ActorSystem(transactionBehavior, "test") ! randomTransactionMessage

    }.as(ExitCode.Success)

}
