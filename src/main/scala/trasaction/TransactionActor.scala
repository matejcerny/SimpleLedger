package trasaction

import akka.actor.typed.ActorSystem
import common.Configuration.AppConfig
import ledger.LedgerActor
import trasaction.Domain.TransactionMessage

object TransactionActor {

  def setup(appConfig: AppConfig): ActorSystem[TransactionMessage] =
    ActorSystem(
      LedgerActor.setup(appConfig),
      "TransactionToLedger"
    )

}
