package common

import java.io.FileNotFoundException

import akka.util.Timeout
import common.Configuration.{AppConfig, DatabaseConfig, buildAppConfig, buildConfig}
import org.scalatest.funsuite.AnyFunSuite

class ConfigurationTest extends AnyFunSuite {

  test("buildConfig") {
    val config = buildConfig("/test.conf").toString
    assert(config == "Config(SimpleConfigObject({}))")

    assertThrows[FileNotFoundException](buildConfig("/not_there.conf"))
  }

  test("buildAppConfig") {
    val path = "/application.conf"
    val config = buildConfig(path)
    val appConfig = AppConfig(
      DatabaseConfig(
        "ORA_USERNAME",
        "ORA_PASSWORD",
        "ORA_CONNECTION_STRING",
        "oracle.jdbc.driver.OracleDriver",
        "acc"
      ),
      Timeout.zero
    )
    assert(buildAppConfig(config) == appConfig)
    assert(buildAppConfig(path) == appConfig)
  }

}
