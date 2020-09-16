package actors

import akka.actor.typed.scaladsl.Behaviors
import akka.actor.typed.{ActorRef, Behavior}
import common.Domain.{FullName, Id}

object IdentityActor {

  case class IdentityRequest(senderId: Id, receiverId: Id, replyTo: ActorRef[IdentityResponse])
  case class IdentityResponse(sender: FullName, receiver: FullName)

  def apply(): Behavior[IdentityRequest] =
    Behaviors.receive { (context, msg) =>
      context.log.info("IdentityRequest received")

      // TODO: find names by ids
      val identityResponse = IdentityResponse(FullName("pan test"), FullName("pan test2"))

      msg.replyTo ! identityResponse
      Behaviors.same
    }

}
