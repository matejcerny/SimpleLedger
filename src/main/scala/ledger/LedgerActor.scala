package ledger

import akka.actor.typed.{ActorSystem, Behavior}
import akka.actor.typed.scaladsl.{AbstractBehavior, ActorContext, Behaviors}
import common.Generator
import persistence.Domain.PersistenceMessage
import persistence.PersistenceActor
import trasaction.Domain.TransactionMessage

class LedgerActor(context: ActorContext[TransactionMessage])
    extends AbstractBehavior[TransactionMessage](context) {

  def onMessage(msg: TransactionMessage): Behavior[TransactionMessage] = {
    //TODO
    // - receiving raw message
    // - getting fullName from IdentityActor
    // - construct PersistenceMessage and send it to PersistenceActor

    val persistenceMessage = Generator.randomPersistenceMessage(msg)

    //context.spawn(PersistenceActor.setup())

    ???
  }

}

object LedgerActor {

  def apply(context: ActorContext[TransactionMessage]): LedgerActor = new LedgerActor(context)

  def setup(): ActorSystem[TransactionMessage] =
    ActorSystem(
      Behaviors.setup[TransactionMessage] { c =>
        LedgerActor(c)
      },
      "TransactionToLedger"
    )

  def sendPersistenceMessage(
    persistenceMessage: PersistenceMessage,
    actorSystem: ActorSystem[PersistenceMessage]
  ): Unit = actorSystem ! persistenceMessage

}
