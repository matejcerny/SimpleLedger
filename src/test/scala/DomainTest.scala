import Domain.{Currency, FullName, Symbol}
import org.scalatest.funsuite.AnyFunSuite

class DomainTest extends AnyFunSuite {

  test("FullName") {
    assertThrows[IllegalArgumentException](FullName("jan"))
    assertThrows[IllegalArgumentException](FullName("j n"))
  }

  test("Symbol") {
    assertThrows[IllegalArgumentException](Symbol("jan"))
    assertThrows[IllegalArgumentException](Symbol("AB"))
    assertThrows[IllegalArgumentException](Symbol("ABCDE"))
  }

  test("Currency") {
    assert(Currency.size == 4)
  }

}
