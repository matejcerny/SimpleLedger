import TransactionActor.TransactionMessage
import akka.actor.typed.{ActorRef, Behavior}
import akka.actor.typed.scaladsl.Behaviors
import common.Domain.{FullName, Id}

object LedgerActor {

  case class IdentityRequest(senderId: Id, receiverId: Id)
  case class IdentityResponse(sender: FullName, receiver: FullName)

  def apply(): Behavior[TransactionMessage] =
    Behaviors.receive { (context, message) =>
      import message.transaction._

      context.log.info("TransactionMessage received by LedgerActor")
      context.log.info(s"Sender: $senderId, receiver: $receiverId")

      // ask for fullName to IdentityActor
      val identityActor = context.spawn(IdentityActor(), "LedgerToIdentity")
      identityActor ! IdentityRequest(senderId, receiverId)


      Behaviors.stopped
    }


  def response(): Behavior[IdentityResponse] =
  Behaviors.receive { (context, message) =>
    context.log.info("IdentityResponse received by LedgerActor")
    context.log.info(s"Sender: ${message.sender}, receiver: ${message.receiver}")

    Behaviors.stopped
  }

}
