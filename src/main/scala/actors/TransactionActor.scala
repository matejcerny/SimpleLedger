package actors

import actors.LedgerActor.TransactionMessage
import akka.actor.typed.Behavior
import akka.actor.typed.scaladsl.Behaviors
import common.Domain.BusinessTime
import utils.Generator.{randomAmount, randomId}

object TransactionActor {

  sealed trait Command
  case object Transaction extends Command
  case object GracefulShutdown extends Command

  def apply(): Behavior[Command] =
    Behaviors.receive {
      case (context, Transaction) =>
        context.log.info("Sending random transaction")
        context
          .spawnAnonymous(LedgerActor())
          .tell(TransactionMessage(randomId, randomId, randomAmount, BusinessTime.now))
        Behaviors.same

      case (context, GracefulShutdown) =>
        context.log.info("Shutting down")
        Behaviors.stopped
    }

}
