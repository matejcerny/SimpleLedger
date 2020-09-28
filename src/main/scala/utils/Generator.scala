package utils

import common.Domain._

import scala.math.BigDecimal.RoundingMode
import scala.util.Random.{between, nextDouble, nextInt}

object Generator {

  val (minId, maxId) = (1, 60)

  def randomId: Id = Id(between(minId, maxId))

  def randomCurrency: Currency = Currency.toList(nextInt(Currency.size))

  def randomAmount: Amount =
    Amount(
      (BigDecimal(nextInt(100000)) + BigDecimal(nextDouble())
        .setScale(8, RoundingMode.HALF_UP)).abs,
      randomCurrency
    )

}
