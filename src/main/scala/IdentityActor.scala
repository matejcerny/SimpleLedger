import LedgerActor.{IdentityRequest, IdentityResponse}
import akka.actor.typed.Behavior
import akka.actor.typed.scaladsl.Behaviors
import common.Domain.FullName

object IdentityActor {

  def apply(): Behavior[IdentityRequest] =
    Behaviors.receive { (context, message) =>

      context.log.info("IdentityRequest received by IdentityActor")
      context.log.info(s"Sender: ${message.senderId}, receiver: ${message.receiverId}")

      // TODO: find names by ids
      val identityResponse = IdentityResponse(FullName("pan test"), FullName("pan test2"))
      val ledgerActor = context.spawn(LedgerActor.response(), "IdentityToLedger")

      ledgerActor ! identityResponse

      Behaviors.stopped
    }

}
