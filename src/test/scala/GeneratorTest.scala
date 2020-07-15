import Domain.Currency
import Generator.randomCurrency
import org.scalatest.funsuite.AnyFunSuite

class GeneratorTest extends AnyFunSuite {

  test("randomPerson") {
    val p = Generator.randomPerson.asString.split(" ")

    assert(p.size == 2)
    assert(Generator.names.contains(p.head))
    assert(Generator.surnames.contains(p(1)))
  }

  test("randomCurrency") {
    assert(Currency.toList.contains(randomCurrency))
  }

}
