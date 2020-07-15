import Generator._
import common.Domain.Currency
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
