package trasaction

import common.Domain.{Amount, BusinessTime, Currency, Id}

object Domain {

  case class Transaction(
    senderId: Id,
    receiver: Id,
    amount: Amount,
    currency: Currency,
    businessTime: BusinessTime = BusinessTime.now
  )

  final case class TransactionMessage(transaction: Transaction)
}
