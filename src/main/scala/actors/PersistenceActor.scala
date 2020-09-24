package actors

import akka.actor.typed.{ActorRef, Behavior}
import akka.actor.typed.scaladsl.{ActorContext, Behaviors}
import cats.effect.IO
import common.Configuration
import common.Domain.{Amount, BusinessTime, Id, Person}
import utils.Database.TransactionData

object PersistenceActor {

  sealed trait Command
  case class PersistenceMessage(
    persistenceId: Id,
    sender: Person,
    receiver: Person,
    amount: Amount,
    businessTime: BusinessTime
  ) extends Command
  case class PersistenceIdRequest(replyTo: ActorRef[Response]) extends Command

  sealed trait Response
  case class PersistenceIdResponse(id: Id) extends Response
  case class FailedPersistenceResponse(reason: String) extends Response

  def apply(): Behavior[Command] =
    Behaviors.receive {
      case (context, msg: PersistenceMessage) =>
        context.log.info(s"PersistenceMessage ${msg.persistenceId.value} received")
        persistData(context, msg).unsafeRunSync()
        Behaviors.same

      case (context, PersistenceIdRequest(replyTo)) =>
        context.log.info(s"TransactionIdRequest received")
        replyTo ! getPersistenceId(context).unsafeRunSync()
        Behaviors.same
    }

  private def persistData(
    context: ActorContext[Command],
    persistenceMessage: PersistenceMessage
  ): IO[Int] =
    Configuration(context.system).database
      .insert(
        TransactionData(
          persistenceMessage.persistenceId,
          persistenceMessage.sender.fullName,
          persistenceMessage.receiver.fullName,
          persistenceMessage.amount,
          persistenceMessage.businessTime
        )
      )

  private def getPersistenceId(context: ActorContext[Command]): IO[Response] =
    Configuration(context.system).database.getNextId
      .map((id: Long) => PersistenceIdResponse(Id(id)))
      .getOrElse(FailedPersistenceResponse("Cannot create PersistenceId"))

}
