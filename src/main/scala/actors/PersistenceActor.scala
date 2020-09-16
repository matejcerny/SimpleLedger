package actors

import akka.actor.typed.Behavior
import akka.actor.typed.scaladsl.Behaviors
import common.Configuration.DatabaseConfig
import common.Domain.{Amount, BusinessTime, Person}
import utils.Database
import utils.Database.TransactionData

object PersistenceActor {

  case class PersistenceMessage(
    sender: Person,
    receiver: Person,
    amount: Amount,
    businessTime: BusinessTime
  )

  def apply(): Behavior[PersistenceMessage] =
    Behaviors.receive { (context, msg) =>
      context.log.info("PersistenceMessage received")
      // create database instance from databaseConfig
      val databaseConfig: DatabaseConfig = ???

      Database(databaseConfig)
        .insert(
          TransactionData(
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
