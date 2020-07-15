import cats.effect.ExitCode
import org.scalatest.funsuite.AnyFunSuite

class SimpleLedgerTest extends AnyFunSuite {

  test("runError") {
    assert(SimpleLedger.run(List.empty).unsafeRunSync() == ExitCode.Error)
  }

//  test("runSuccess") {
//    assert(SimpleLedger.run(List("/application.conf")).unsafeRunSync() == ExitCode.Success)
//  }

}
