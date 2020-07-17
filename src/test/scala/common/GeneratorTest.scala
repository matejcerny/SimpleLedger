package common

import common.Domain.{Amount, Currency}
import common.Generator._
import org.scalacheck.Prop._
import org.scalacheck.Properties
import org.scalatest.funsuite.AnyFunSuite

class GeneratorTest extends AnyFunSuite {

  test("randomPerson") {
    val p = randomPerson.asString.split(" ")

    assert(p.size == 2)
    assert(names.contains(p.head))
    assert(surnames.contains(p(1)))
  }

  test("randomCurrency") {
    assert(Currency.toList.contains(randomCurrency))
  }

}

object GeneratorTest extends Properties("common.GeneratorTest") {

  property("randomAmount") = forAll { _: Int =>
    randomAmount.value >= Amount.min.value && randomAmount.value <= Amount.max.value
  }

}
