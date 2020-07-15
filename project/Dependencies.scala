import sbt._

object Dependencies {

  object Versions {
    val Akka = "2.6.7"
    val Cats = "2.1.1"
    val Config = "1.0.1"
    val Doobie = "0.9.0"
    val Logback = "1.2.3"
    val ScalaLogging = "3.9.2"
    val ScalaTest = "3.1.2"
    val SLF4J = "1.7.9"
  }

  val Akka = Seq("com.typesafe.akka" %% "akka-cluster-typed" % Versions.Akka)

  val Cats = Seq(
    "org.typelevel" %% "cats-core" % Versions.Cats,
    "org.typelevel" %% "cats-effect" % Versions.Cats
  )

  val Config = Seq("com.github.andyglow" %% "typesafe-config-scala" % Versions.Config)

  val Doobie = Seq(
    "org.tpolecat" %% "doobie-core" % Versions.Doobie,
    "org.tpolecat" %% "doobie-postgres" % Versions.Doobie,
    "org.tpolecat" %% "doobie-specs2" % Versions.Doobie % Test,
    "org.tpolecat" %% "doobie-scalatest" % Versions.Doobie % Test
  )

  val Logging = Seq(
    "ch.qos.logback" % "logback-classic" % Versions.Logback,
    "com.typesafe.scala-logging" %% "scala-logging" % Versions.ScalaLogging,
    "org.slf4j" % "slf4j-api" % Versions.SLF4J
  )

  val ScalaTest = Seq("org.scalatest" %% "scalatest" % Versions.ScalaTest % Test)

}
