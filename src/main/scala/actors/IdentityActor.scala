package actors

import akka.actor.typed.scaladsl.Behaviors
import akka.actor.typed.{ActorRef, Behavior}
import common.Configuration
import common.Domain.{FullName, Id}

object IdentityActor {

  sealed trait Response
  case class IdentityRequest(senderId: Id, receiverId: Id, replyTo: ActorRef[Response])
  case class IdentityResponse(sender: FullName, receiver: FullName) extends Response
  case object PersonNotFound extends Response

  def apply(): Behavior[IdentityRequest] =
    Behaviors.receive { (context, msg) =>
      context.log.info("IdentityRequest received")

      val db = Configuration(context.system).database
      (
        for {
          receiver <- db.getPersonFullName(msg.receiverId)
          sender <- db.getPersonFullName(msg.senderId)
        } yield IdentityResponse(FullName(receiver), FullName(sender))
      ).getOrElse(PersonNotFound)
        .map(msg.replyTo.tell)
        .unsafeRunSync()

      Behaviors.same
    }

}
