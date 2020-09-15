import akka.actor.typed.scaladsl.Behaviors
import akka.actor.typed.{ActorRef, Behavior}
import common.Domain.{Amount, BusinessTime, Id}

object TransactionActor {

  case class Transaction(
    senderId: Id,
    receiverId: Id,
    amount: Amount,
    businessTime: BusinessTime = BusinessTime.now
  )
  case class TransactionMessage(transaction: Transaction, from: ActorRef[Transaction])

  def apply(): Behavior[Transaction] =
    Behaviors.receive { (context, message) =>
      context.log.info("Transaction received by TransactionActor")

      val ledgerActor = context.spawn(LedgerActor(), "TransactionToLedger")

      ledgerActor ! TransactionMessage(message, context.self)
      Behaviors.stopped
    }

}
