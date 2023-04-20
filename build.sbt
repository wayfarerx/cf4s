import Dependencies.*

ThisBuild / organization := "net.wayfarerx"
ThisBuild / version := "0.2.2"
ThisBuild / scalaVersion := "3.2.2"

/** The aggregation of the projects that define cf4s. */
lazy val cf4s = (project in file(".")).aggregate(
  generator,
  common,
  s3
)

/** The generator for cf4s. */
lazy val generator = project.settings(
  name := "cf4s-generator",
  libraryDependencies ++= Seq(
    AwsCore,
    CirceCore,
    CirceParser,
    Scopt,
    Zio,
    ZioInteropCats,
    ZioLogging,
    ZioTest,
    ZioTestSbt,
    ZioTestMagnolia
  ),
  testFrameworks += new TestFramework("zio.test.sbt.ZTestFramework")//,
  //Compile / run / fork := true,
  //mainClass := Some("net.wayfarerx.cf4s.generator.main.Main")
)

/** The common model for cf4s. */
lazy val common = project.settings(
  name := "cf4s-common",
  libraryDependencies ++= Seq(
    CirceCore
  )
)

/** The S3 model for cf4s. */
lazy val s3 = project.settings(
  name := "cf4s-s3"
).dependsOn(common)