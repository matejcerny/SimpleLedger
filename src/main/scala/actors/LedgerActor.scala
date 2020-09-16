package actors

import actors.IdentityActor.IdentityRequest
import actors.PersistenceActor.PersistenceMessage
import akka.actor.typed.Behavior
import akka.actor.typed.scaladsl.Behaviors
import akka.util.Timeout
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

  def apply()(implicit timeout: Timeout): Behavior[Command] =
    Behaviors.setup[Command] { context =>
      Behaviors.receiveMessage {
        case TransactionWithIdentities(sender, receiver, amount, businessTime) =>
          context.log.info("IdentityMessage received")

          val persistenceActor = context.spawn(PersistenceActor(), "LedgerToPersistence")
          persistenceActor ! PersistenceMessage(sender, receiver, amount, businessTime)

          Behaviors.same

        case TransactionMessage(senderId, receiverId, amount, businessTime) =>
          context.log.info("TransactionMessage received")
          val identityActor = context.spawn(IdentityActor(), "LedgerToIdentity")

          context.ask(identityActor, a => IdentityRequest(senderId, receiverId, a)) {
            case Success(r) =>
              TransactionWithIdentities(
                Person(senderId, r.sender),
                Person(receiverId, r.receiver),
                amount,
                businessTime
              )
            case Failure(_) => ???
          }

          Behaviors.same
      }

    }

}
