package trasaction

import common.Domain.{Amount, BusinessTime, Id}

object Domain {

  case class Transaction(
    senderId: Id,
    receiver: Id,
    amount: Amount,
    businessTime: BusinessTime = BusinessTime.now
  )

  final case class TransactionMessage(transaction: Transaction)
}
