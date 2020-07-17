package persistence

import akka.actor.typed.Behavior
import akka.actor.typed.scaladsl.{AbstractBehavior, ActorContext, Behaviors}
import persistence.Domain.PersistenceMessage

class PersistenceActor(context: ActorContext[PersistenceMessage], persistenceAPI: PersistenceAPI)
    extends AbstractBehavior[PersistenceMessage](context) {

  def onMessage(msg: PersistenceMessage): Behavior[PersistenceMessage] = {
    context.log.info(msg.persistence.sender.asString)
    persistenceAPI.insert(msg.persistence).unsafeRunSync()

    Behaviors.stopped
  }

}

object PersistenceActor {

  def setup(persistenceAPI: PersistenceAPI): Behavior[PersistenceMessage] =
    Behaviors.setup[PersistenceMessage] { c =>
      PersistenceActor(c, persistenceAPI)
    }

  def apply(
    context: ActorContext[PersistenceMessage],
    persistenceAPI: PersistenceAPI
  ): PersistenceActor = new PersistenceActor(context, persistenceAPI)

}
