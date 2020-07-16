package common

import java.time.LocalDateTime

object Domain {

  case class FullName(value: String) {
    require(value.length > 3 && value.contains(" "))
  }

  case class Person(fullName: FullName) { val asString: String = fullName.value }

  case class Amount(value: BigDecimal) {
    require(value.precision <= 38 && value.scale <= 8)
  }
  object Amount {
    def min: Amount = Amount(BigDecimal("0.00000001"))
    def max: Amount = Amount(BigDecimal("999999999999999999999999999999"))
  }

  case class Symbol(value: String) {
    require(value.length >= 3 && value.length <= 4 && value == value.toUpperCase)
  }

  sealed trait Currency { def symbol: Symbol }

  object Currency extends Iterable[Currency] {
    case object Bitcoin extends Currency { val symbol: Symbol = Symbol("BTC") }
    case object Litecoin extends Currency { val symbol: Symbol = Symbol("LTC") }
    case object Ethereum extends Currency { val symbol: Symbol = Symbol("ETH") }
    case object Cardano extends Currency { val symbol: Symbol = Symbol("ADA") }

    def iterator: Iterator[Currency] = Iterator(Bitcoin, Litecoin, Ethereum, Cardano)
  }

  case class BusinessTime(value: LocalDateTime)
  object BusinessTime { def now: BusinessTime = BusinessTime(LocalDateTime.now) }

  case class Transaction(
    sender: Person,
    receiver: Person,
    amount: Amount,
    currency: Currency,
    businessTime: BusinessTime = BusinessTime.now
  )

  final case class TransactionMessage(transaction: Transaction)
}
