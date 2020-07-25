package ledger

import akka.actor.typed.Behavior
import akka.actor.typed.scaladsl.{AbstractBehavior, ActorContext, Behaviors}
import common.Configuration.AppConfig
import common.Generator
import persistence.{PersistenceAPI, PersistenceActor}
import trasaction.Domain.TransactionMessage

class LedgerActor(context: ActorContext[TransactionMessage], appConfig: AppConfig)
    extends AbstractBehavior[TransactionMessage](context) {

  def onMessage(msg: TransactionMessage): Behavior[TransactionMessage] = {
    val persistenceActor = context.spawn(
      PersistenceActor.setup(PersistenceAPI(appConfig.databaseConfig)),
      "LedgerToPersistence"
    )

    // TODO: get fullName from IdentityActor
    persistenceActor ! Generator.randomPersistenceMessage(msg)

    Behaviors.stopped
  }

}

object LedgerActor {

  def apply(context: ActorContext[TransactionMessage], appConfig: AppConfig): LedgerActor =
    new LedgerActor(context, appConfig)

  def setup(appConfig: AppConfig): Behavior[TransactionMessage] =
    Behaviors.setup[TransactionMessage] { c =>
      LedgerActor(c, appConfig)
    }

}
