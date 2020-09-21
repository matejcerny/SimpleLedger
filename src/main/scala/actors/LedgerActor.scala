package actors

import actors.IdentityActor.{FailedResponse, IdentityRequest, IdentityResponse}
import actors.PersistenceActor.PersistenceMessage
import akka.actor.typed.Behavior
import akka.actor.typed.scaladsl.{ActorContext, Behaviors}
import akka.util.Timeout
import common.Configuration
import common.Domain.{Amount, BusinessTime, Id, Person}

import scala.util.{Failure, Success}

object LedgerActor {

  sealed trait Command
  case class TransactionMessage(
    transactionId: Id,
    senderId: Id,
    receiverId: Id,
    amount: Amount,
    businessTime: BusinessTime
  ) extends Command

  case class TransactionWithIdentities(
    transactionId: Id,
    sender: Person,
    receiver: Person,
    amount: Amount,
    businessTime: BusinessTime
  ) extends Command

  case class FailedMessage(reason: String) extends Command

  def apply(): Behavior[Command] =
    Behaviors.setup[Command] { context =>
      Behaviors.receiveMessage {
        case transactionMessage: TransactionMessage =>
          processTransaction(context, transactionMessage)
          Behaviors.same

        case TransactionWithIdentities(id, sender, receiver, amount, businessTime) =>
          context.log.info(s"IdentityMessage ${id.value} received")
          context
            .spawn(PersistenceActor(), "LedgerToPersistence")
            .tell(PersistenceMessage(id, sender, receiver, amount, businessTime))
          Behaviors.same

        case FailedMessage(reason) =>
          context.log.error(reason)
          Behaviors.same
      }
    }

  private def processTransaction(
    context: ActorContext[Command],
    transactionMessage: TransactionMessage
  ): Unit = {
    import transactionMessage._
    implicit val timeout: Timeout = Configuration(context.system).timeout

    context.log.info(s"TransactionMessage ${transactionId.value} received")

    val identityActor = context.spawn(IdentityActor(), "LedgerToIdentity")

    context.ask(identityActor, a => IdentityRequest(senderId, receiverId, a)) {
      case Success(IdentityResponse(sender, receiver)) =>
        TransactionWithIdentities(
          transactionId,
          Person(senderId, sender),
          Person(receiverId, receiver),
          amount,
          businessTime
        )
      case Success(FailedResponse(reason)) => FailedMessage(reason)
      case Failure(e) => FailedMessage(e.getMessage)
    }
  }

}
