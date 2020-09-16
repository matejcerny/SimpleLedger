package actors

import actors.LedgerActor.TransactionMessage
import akka.actor.typed.Behavior
import akka.actor.typed.scaladsl.Behaviors
import akka.util.Timeout
import common.Domain.{Amount, BusinessTime, Id}

object TransactionActor {

  case class Transaction(
    senderId: Id,
    receiverId: Id,
    amount: Amount,
    businessTime: BusinessTime = BusinessTime.now
  )

  def apply()(implicit timeout: Timeout): Behavior[Transaction] =
    Behaviors.receive { (context, msg) =>
      context.log.info("Transaction received")

      val ledgerActor = context.spawn(LedgerActor(), "TransactionToLedger")

      ledgerActor ! TransactionMessage(msg.senderId, msg.receiverId, msg.amount, msg.businessTime)
      Behaviors.stopped
    }

}
