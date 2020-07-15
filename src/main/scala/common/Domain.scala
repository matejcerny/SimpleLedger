package common

object Domain {

  case class FullName(value: String) {
    require(value.length > 3 && value.contains(" "))
  }

  case class Person(fullName: FullName) { val asString: String = fullName.value }

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

  case class Transaction(
    sender: Person,
    receiver: Person,
    currency: Currency
  )

  final case class TransactionMessage(transaction: Transaction)
}
