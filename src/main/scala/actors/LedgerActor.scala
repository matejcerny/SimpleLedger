package actors

import actors.IdentityActor.{PersonNotFound, IdentityRequest, IdentityResponse}
import actors.PersistenceActor.{PersistenceIdFailed, PersistenceIdRequest, PersistenceIdResponse, PersistenceMessage}
import akka.actor.typed.Behavior
import akka.actor.typed.scaladsl.{ActorContext, Behaviors}
import akka.util.Timeout
import common.Configuration
import common.Domain.{Amount, BusinessTime, Id, Person}

import scala.util.{Failure, Success}

object LedgerActor {

  sealed trait Command
  case class TransactionMessage(
    senderId: Id,
    receiverId: Id,
    amount: Amount,
    businessTime: BusinessTime
  ) extends Command

  case class TransactionWithIdentities(
    sender: Person,
    receiver: Person,
    amount: Amount,
    businessTime: BusinessTime
  ) extends Command

  case class TransactionForPersistence(
    id: Id,
    sender: Person,
    receiver: Person,
    amount: Amount,
    businessTime: BusinessTime
  ) extends Command

  case class FailedLedgerMessage(reason: String) extends Command

  def apply(): Behavior[Command] =
    Behaviors.setup[Command] { context =>
      Behaviors.receiveMessage {
        case msg: TransactionMessage =>
          context.log.info(s"TransactionMessage received")
          askForIdentities(context, msg)
          Behaviors.same

        case msg: TransactionWithIdentities =>
          context.log.info(s"TransactionWithIdentities received")
          askForPersistenceId(context, msg)
          Behaviors.same

        case TransactionForPersistence(id, sender, receiver, amount, businessTime) =>
          context.log.info(s"TransactionForPersistence received")
          context
            .spawn(PersistenceActor(), "LedgerToPersistence")
            .tell(PersistenceMessage(id, sender, receiver, amount, businessTime))
          Behaviors.same

        case FailedLedgerMessage(reason) =>
          context.log.error(reason)
          Behaviors.same
      }
    }

  private def askForIdentities(
    context: ActorContext[Command],
    transactionMessage: TransactionMessage
  ): Unit = {
    import transactionMessage._
    implicit val timeout: Timeout = Configuration(context.system).timeout

    val identityActor = context.spawn(IdentityActor(), "LedgerToIdentity")

    context.ask(identityActor, a => IdentityRequest(senderId, receiverId, a)) {
      case Success(IdentityResponse(sender, receiver)) =>
        TransactionWithIdentities(
          Person(senderId, sender),
          Person(receiverId, receiver),
          amount,
          businessTime
        )
      case Success(PersonNotFound) => FailedLedgerMessage("Person not found")
      case Failure(e) => FailedLedgerMessage(e.getMessage)
    }
  }

  private def askForPersistenceId(
    context: ActorContext[Command],
    transactionWithIdentities: TransactionWithIdentities
  ): Unit = {
    import transactionWithIdentities._
    implicit val timeout: Timeout = Configuration(context.system).timeout

    val persistenceActor = context.spawn(PersistenceActor(), "LedgerToPersistenceId")

    context.ask(persistenceActor, PersistenceIdRequest) {
      case Success(PersistenceIdResponse(id)) =>
        TransactionForPersistence(id, sender, receiver, amount, businessTime)

      case Success(PersistenceIdFailed) => FailedLedgerMessage("Cannot create PersistenceId")
      case Failure(e) => FailedLedgerMessage(e.getMessage)
    }
  }

}
