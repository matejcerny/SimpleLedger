package persistence

import akka.actor.typed.Behavior
import akka.actor.typed.scaladsl.{AbstractBehavior, ActorContext}
import common.Domain.TransactionMessage

class PersistenceActor(context: ActorContext[TransactionMessage], persistenceAPI: PersistenceAPI)
    extends AbstractBehavior[TransactionMessage](context) {

  def onMessage(msg: TransactionMessage): Behavior[TransactionMessage] = {
    context.log.info(msg.transaction.sender.asString)
    persistenceAPI.insert(msg.transaction)

    this
  }

}

object PersistenceActor {

  def apply(
    context: ActorContext[TransactionMessage],
    persistenceAPI: PersistenceAPI
  ): PersistenceActor = new PersistenceActor(context, persistenceAPI)

}
