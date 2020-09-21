package actors

import akka.actor.typed.Behavior
import akka.actor.typed.scaladsl.Behaviors
import common.Configuration
import common.Domain.{Amount, BusinessTime, Id, Person}
import utils.Database.TransactionData

object PersistenceActor {

  case class PersistenceMessage(
    transactionId: Id,
    sender: Person,
    receiver: Person,
    amount: Amount,
    businessTime: BusinessTime
  )

  def apply(): Behavior[PersistenceMessage] =
    Behaviors.receive { (context, msg) =>
      context.log.info(s"PersistenceMessage ${msg.transactionId.value} received")

      Configuration(context.system).database
        .insert(
          TransactionData(
            msg.transactionId,
            msg.sender.fullName,
            msg.receiver.fullName,
            msg.amount,
            msg.businessTime
          )
        )
        .unsafeRunSync()

      Behaviors.same
    }

}
