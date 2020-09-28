package utils

import common.Domain.{Amount, Currency}
import org.scalacheck.Prop._
import org.scalacheck.Properties
import utils.Generator._
import scala.util.Random.between

object GeneratorTest extends Properties("common.GeneratorTest") {

  property("randomId") = forAll { _: Int =>
    val r = randomId.value
    r >= minId && r < maxId
  }

  property("randomAmount") = forAll { _: Int =>
    val r = randomAmount.value
    r >= Amount.min.value && r <= Amount.max.value
  }

  property("randomCurrency") = Currency.toList.contains(randomCurrency)

}
