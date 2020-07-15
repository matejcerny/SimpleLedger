import Dependencies._

Global / name := "SimpleLedger"
Global / organization := "cz.matejcerny"
Global / scalaVersion := "2.13.3"
Global / assemblyJarName := s"${name.value}.jar"

lazy val SimpleLedger = project
  .in(file("."))
  .settings(
    libraryDependencies ++= Cats ++ Config ++ Doobie ++ Logging ++ ScalaTest
  )


Global / scalacOptions ++= Seq(
  "-deprecation", // Emit warning and location for usages of deprecated APIs.
  "-feature", // Emit warning and location for usages of features that should be imported explicitly.
  "-language:implicitConversions", // Allow definition of implicit functions called views
  "-language:higherKinds", // Allow higher-kinded types
  "-Ywarn-unused:imports" // Warn if an import selector is not referenced.
)