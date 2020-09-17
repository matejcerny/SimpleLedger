package utils

import common.Domain.{Amount, Currency}
import org.scalacheck.Prop._
import org.scalacheck.Properties
import utils.Generator._

object GeneratorTest extends Properties("common.GeneratorTest") {

  property("randomAmount") = forAll { _: Int =>
    randomAmount.value >= Amount.min.value && randomAmount.value <= Amount.max.value
  }

  property("randomPerson") = {
    val p = randomPerson.asString.split(" ")
    p.size == 2 && names.contains(p.head) && surnames.contains(p(1))
  }

  property("randomCurrency") = Currency.toList.contains(randomCurrency)

}
