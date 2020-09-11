package persistence

import common.Domain.{Amount, BusinessTime, Person}

object Domain {

  case class Persistence(
    sender: Person,
    receiver: Person,
    amount: Amount,
    businessTime: BusinessTime
  )

  final case class PersistenceMessage(persistence: Persistence)
}
