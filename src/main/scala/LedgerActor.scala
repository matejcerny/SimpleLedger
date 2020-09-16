import IdentityActor.IdentityRequest
import akka.actor.typed.Behavior
import akka.actor.typed.scaladsl.Behaviors
import akka.util.Timeout
import common.Domain.{Amount, BusinessTime, Id, Person}

import scala.util.Success

object LedgerActor {

  sealed trait Command
  case class TransactionMessage(
    senderId: Id,
    receiverId: Id,
    amount: Amount,
    businessTime: BusinessTime
  ) extends Command
  case class IdentityMessage(sender: Person, receiver: Person) extends Command

  def apply()(implicit timeout: Timeout): Behavior[Command] =
    Behaviors.setup[Command] { context =>
      Behaviors.receiveMessage {
        case IdentityMessage(sender, receiver) =>
          context.log.info("IdentityMessage received by LedgerActor")
          context.log.info(s"Sender: $sender, receiver: $receiver")

          // TODO: persist persons

          Behaviors.same

        case TransactionMessage(senderId, receiverId, _, _) =>
          context.log.info("TransactionMessage received by LedgerActor")
          val identityActor = context.spawn(IdentityActor(), "LedgerToIdentity")

          context.ask(identityActor, a => IdentityRequest(senderId, receiverId, a)) {
            case Success(r) =>
              IdentityMessage(
                Person(senderId, r.sender),
                Person(receiverId, r.receiver)
              )
          }

          Behaviors.same
      }

    }

}
