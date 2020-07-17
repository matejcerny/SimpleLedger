package trasaction

import akka.actor.typed.ActorSystem
import trasaction.Domain.TransactionMessage

object TransactionActor {

  def sendTransactionMessage(
    transactionMessage: TransactionMessage,
    actorSystem: ActorSystem[TransactionMessage]
  ): Unit = actorSystem ! transactionMessage

}
