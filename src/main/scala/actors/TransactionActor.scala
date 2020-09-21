package actors

import actors.LedgerActor.TransactionMessage
import akka.actor.typed.Behavior
import akka.actor.typed.scaladsl.{ActorContext, Behaviors}
import common.Configuration
import common.Domain.{Amount, BusinessTime, Id}

object TransactionActor {

  sealed trait Command
  case class Transaction(
    senderId: Id,
    receiverId: Id,
    amount: Amount,
    businessTime: BusinessTime = BusinessTime.now
  ) extends Command
  case class Termination(reason: String) extends Command
  case object GracefulShutdown extends Command

  def apply(): Behavior[Command] =
    Behaviors.receive {
      case (context, transaction: Transaction) =>
        context.log.info("Transaction received")
        processTransaction(context, transaction)
        Behaviors.same

      case (context, Termination(reason)) =>
        context.log.error(reason)
        Behaviors.same

      case (_, GracefulShutdown) =>
        Behaviors.stopped
    }

  private def processTransaction(context: ActorContext[Command], transaction: Transaction): Unit = {
    // get ID from database
    val message = Configuration(context.system).database.getNextId
      .map { id: Long =>
        TransactionMessage(
          Id(id),
          transaction.senderId,
          transaction.receiverId,
          transaction.amount,
          transaction.businessTime
        )
      }
      .getOrElse(Termination("TransactionId not found"))
      .unsafeRunSync()

    message match {
      case t: TransactionMessage =>
        context
          .spawn(LedgerActor(), s"TransactionToLedger_${t.transactionId.value}")
          .tell(t)

      case t: Termination => context.self.tell(t)
    }
  }

}
