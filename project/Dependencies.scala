import sbt._

object Dependencies {

  object Versions {
    val Akka = "2.6.7"
    val Cats = "2.1.1"
    val Config = "1.0.1"
    val Doobie = "0.9.0"
    val Logback = "1.2.3"
    val ScalaCheck = "1.14.1"
    val ScalaLogging = "3.9.2"
    val ScalaTest = "3.1.2"
    val SLF4J = "1.7.9"
  }

  object Modules {
    val Akka = "com.typesafe.akka"
    val Cats = "org.typelevel"
    val Config = "com.github.andyglow"
    val Doobie = "org.tpolecat"
    val Logback = "ch.qos.logback"
    val ScalaCheck = "org.scalacheck"
    val ScalaLogging = "com.typesafe.scala-logging"
    val ScalaTest = "org.scalatest"
    val SLF4J = "org.slf4j"
  }

  val Akka = Seq(
    Modules.Akka %% "akka-cluster-typed" % Versions.Akka
  )

  val Cats = Seq(
    Modules.Cats %% "cats-core" % Versions.Cats,
    Modules.Cats %% "cats-effect" % Versions.Cats
  )

  val Config = Seq(
    Modules.Config %% "typesafe-config-scala" % Versions.Config
  )

  val Doobie = Seq(
    Modules.Doobie %% "doobie-core" % Versions.Doobie,
    Modules.Doobie %% "doobie-postgres" % Versions.Doobie,
    Modules.Doobie %% "doobie-specs2" % Versions.Doobie % Test,
    Modules.Doobie %% "doobie-scalatest" % Versions.Doobie % Test
  )

  val Logging = Seq(
    Modules.Logback % "logback-classic" % Versions.Logback,
    Modules.ScalaLogging %% "scala-logging" % Versions.ScalaLogging,
    Modules.SLF4J % "slf4j-api" % Versions.SLF4J
  )

  val Testing = Seq(
    Modules.ScalaTest %% "scalatest" % Versions.ScalaTest % Test,
    Modules.ScalaCheck %% "scalacheck" % Versions.ScalaCheck % Test,
    Modules.Akka %% "akka-actor-testkit-typed" % Versions.Akka % Test
  )

}
