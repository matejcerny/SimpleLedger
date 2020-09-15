import TransactionActor.Transaction
import common.Domain._
import persistence.Domain.{Persistence, PersistenceMessage}

import scala.math.BigDecimal.RoundingMode
import scala.util.Random.{nextDouble, nextInt}

object Generator {

  val names: List[String] = List(
    "Albert", "Anita", "Baltazar", "Boris", "Daisy", "Egon", "Fjodor", "Gerda", "Hugo",
    "Izabela", "Justýna", "Kvido", "Lubor", "Milena", "Nikita", "Pius", "Regína", "Tristan"
  )
  val surnames: List[String] = List(
    "Hrnčíř", "Kadlec", "Krejčí", "Mlynář", "Sedlák", "Ševčík", "Tesař", "Zedníček",
  )

  def randomId: Id = Id(1)

  def randomPerson: Person = Person(
    randomId,
    FullName(
      names(nextInt(names.length)) + " " + surnames(nextInt(surnames.length))
    )
  )

  def randomCurrency: Currency = Currency.toList(nextInt(Currency.size))

  def randomAmount: Amount =
    Amount(
      (BigDecimal(nextInt(100000)) + BigDecimal(nextDouble())
        .setScale(8, RoundingMode.HALF_UP)).abs,
      randomCurrency
    )

  def randomTransactionMessage: Transaction =
    Transaction(
      randomId,
      randomId,
      randomAmount
    )

  def randomPersistenceMessage: PersistenceMessage =
    PersistenceMessage(
      Persistence(
        randomPerson,
        randomPerson,
        randomAmount,
        BusinessTime.now
      )
    )

//  def randomPersistenceMessage(transactionMessage: TransactionMessage): PersistenceMessage =
//    PersistenceMessage(
//      Persistence(
//        randomPerson,
//        randomPerson,
//        transactionMessage.transaction.amount,
//        transactionMessage.transaction.businessTime
//      )
//    )
//
//  def randomPersistenceMessage(
//    sender: Person,
//    receiver: Person,
//    transactionMessage: TransactionMessage
//  ): PersistenceMessage =
//    PersistenceMessage(
//      Persistence(
//        sender,
//        receiver,
//        transactionMessage.transaction.amount,
//        transactionMessage.transaction.businessTime
//      )
//    )

}
