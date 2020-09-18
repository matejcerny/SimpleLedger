package actors

import akka.actor.typed.scaladsl.Behaviors
import akka.actor.typed.{ActorRef, Behavior}
import common.Configuration
import common.Domain.{FullName, Id}

object IdentityActor {

  case class IdentityRequest(senderId: Id, receiverId: Id, replyTo: ActorRef[IdentityResponse])
  case class IdentityResponse(sender: FullName, receiver: FullName)

  def apply(): Behavior[IdentityRequest] =
    Behaviors.receive { (context, msg) =>
      context.log.info("IdentityRequest received")

      val db = Configuration(context.system).database
      (
        for {
          receiver <- db.getPersonFullName(msg.receiverId)
          sender <- db.getPersonFullName(msg.senderId)
        } yield IdentityResponse(FullName(receiver), FullName(sender))
      ).value.unsafeRunSync() match {
        case Some(identityResponse) => msg.replyTo ! identityResponse
        case None => ???
      }

      Behaviors.same
    }

}
