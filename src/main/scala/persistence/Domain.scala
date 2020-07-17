package persistence

import common.Domain.{Amount, BusinessTime, Currency, Person}

object Domain {

  case class Persistence(
    sender: Person,
    receiver: Person,
    amount: Amount,
    currency: Currency,
    businessTime: BusinessTime
  )

  final case class PersistenceMessage(persistence: Persistence)
}
