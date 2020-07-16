import common.Domain.{Amount, Currency, FullName, Person}

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

  def randomPerson: Person = Person(
    FullName(
      names(nextInt(names.length)) + " " + surnames(nextInt(surnames.length))
    )
  )

  def randomCurrency: Currency = Currency.toList(nextInt(Currency.size))

  def randomAmount: Amount =
    Amount(
      (BigDecimal(nextInt(100000)) + BigDecimal(nextDouble()).setScale(8, RoundingMode.HALF_UP)).abs
    )
}
