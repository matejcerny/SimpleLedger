import akka.actor.typed.{ActorRef, Behavior}
import akka.actor.typed.scaladsl.Behaviors
import common.Domain.{FullName, Id}

object IdentityActor {

  sealed trait Command
  case class IdentityRequest(senderId: Id, receiverId: Id, replyTo: ActorRef[IdentityResponse]) extends Command
  case class IdentityResponse(sender: FullName, receiver: FullName)

  def apply(): Behavior[Command] =
    Behaviors.receiveMessage[Command] {
      case IdentityRequest(senderId, receiverId, replyTo) =>
        // TODO: find names by ids
        val identityResponse = IdentityResponse(FullName("pan test"), FullName("pan test2"))

        replyTo ! identityResponse
        Behaviors.stopped
    }

}
