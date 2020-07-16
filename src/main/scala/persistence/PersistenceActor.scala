package persistence

import akka.actor.typed.Behavior
import akka.actor.typed.scaladsl.{AbstractBehavior, ActorContext, Behaviors}
import common.Domain.TransactionMessage

class PersistenceActor(context: ActorContext[TransactionMessage], persistenceAPI: PersistenceAPI)
    extends AbstractBehavior[TransactionMessage](context) {

  def onMessage(msg: TransactionMessage): Behavior[TransactionMessage] = {
    context.log.info(msg.transaction.sender.asString)
    persistenceAPI.insert(msg.transaction).unsafeRunSync()

    Behaviors.stopped
  }

}

object PersistenceActor {

  def apply(
    context: ActorContext[TransactionMessage],
    persistenceAPI: PersistenceAPI
  ): PersistenceActor = new PersistenceActor(context, persistenceAPI)

}
