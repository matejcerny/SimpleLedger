package actors

import actors.IdentityActor.{IdentityRequest, IdentityResponse, FailedResponse}
import actors.PersistenceActor.PersistenceMessage
import akka.actor.typed.Behavior
import akka.actor.typed.scaladsl.Behaviors
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

  case class FailedMessage(reason: String) extends Command

  def apply(): Behavior[Command] =
    Behaviors.setup[Command] { context =>
      Behaviors.receiveMessage {
        case TransactionMessage(senderId, receiverId, amount, businessTime) =>
          context.log.info("TransactionMessage received")

          val identityActor = context.spawn(IdentityActor(), "LedgerToIdentity")
          implicit val timeout: Timeout = Configuration(context.system).timeout

          context.ask(identityActor, a => IdentityRequest(senderId, receiverId, a)) {
            case Success(IdentityResponse(sender, receiver)) =>
              TransactionWithIdentities(
                Person(senderId, sender),
                Person(receiverId, receiver),
                amount,
                businessTime
              )
            case Success(FailedResponse(reason)) => FailedMessage(reason)
            case Failure(e) => FailedMessage(e.getMessage)
          }
          Behaviors.same

        case TransactionWithIdentities(sender, receiver, amount, businessTime) =>
          context.log.info("IdentityMessage received")

          val persistenceActor = context.spawn(PersistenceActor(), "LedgerToPersistence")
          persistenceActor ! PersistenceMessage(sender, receiver, amount, businessTime)
          Behaviors.same

        case FailedMessage(reason) =>
          context.log.error(reason)
          Behaviors.same
      }
    }

}
