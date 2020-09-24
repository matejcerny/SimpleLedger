package actors

import actors.LedgerActor.TransactionMessage
import akka.actor.typed.Behavior
import akka.actor.typed.scaladsl.Behaviors
import common.Domain.{Amount, BusinessTime, Id}

object TransactionActor {

  sealed trait Command
  case class Transaction(senderId: Id, receiverId: Id, amount: Amount) extends Command
  case object GracefulShutdown extends Command

  def apply(): Behavior[Command] =
    Behaviors.receive {
      case (context, Transaction(senderId, receiverId, amount)) =>
        context.log.info("Transaction received")
        context
          .spawnAnonymous(LedgerActor())
          .tell(TransactionMessage(senderId, receiverId, amount, BusinessTime.now))
        Behaviors.same

      case (context, GracefulShutdown) =>
        context.log.info("Shutting down")
        Behaviors.stopped
    }

}
