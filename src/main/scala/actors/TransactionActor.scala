package actors

import actors.LedgerActor.TransactionMessage
import akka.actor.typed.Behavior
import akka.actor.typed.scaladsl.Behaviors
import common.Domain.{Amount, BusinessTime, Id}

object TransactionActor {

  sealed trait Command
  case class Transaction(
    senderId: Id,
    receiverId: Id,
    amount: Amount,
    businessTime: BusinessTime = BusinessTime.now
  ) extends Command
  case object GracefulShutdown extends Command

  def apply(): Behavior[Command] =
    Behaviors.receive {
      case (context, msg: Transaction) =>
        context.log.info("Transaction received")
        val ledgerActor = context.spawn(LedgerActor(), s"TransactionToLedger")

        ledgerActor ! TransactionMessage(msg.senderId, msg.receiverId, msg.amount, msg.businessTime)
        Behaviors.same

      case (_, GracefulShutdown) =>
        Behaviors.stopped
    }

}
