import sbt.*
object Dependencies {

  // Compile Dependencies

  val AwsGroup = "com.amazonaws"
  val AwsVersion = "1.12.472"
  lazy val AwsCore = AwsGroup % "aws-java-sdk-core" % AwsVersion

  val CirceGroup = "io.circe"
  val CirceVersion = "0.14.5"
  lazy val CirceCore = CirceGroup %% "circe-core" % CirceVersion
  lazy val CirceParser = CirceGroup %% "circe-parser" % CirceVersion

  lazy val Scopt = "com.github.scopt" %% "scopt" % "4.1.0"

  val ZioGroup = "dev.zio"
  val ZioVersion = "2.0.13"
  lazy val Zio = ZioGroup %% "zio" % ZioVersion
  lazy val ZioInteropCats = ZioGroup %% "zio-interop-cats" % "23.0.03"
  lazy val ZioLogging = ZioGroup %% "zio-logging" % "2.1.12"

  // Test Dependencies

  lazy val ScalaTest = "org.scalatest" %% "scalatest" % "3.2.15" % Test

  lazy val ZioTest = ZioGroup %% "zio-test" % ZioVersion % Test
  lazy val ZioTestSbt = ZioGroup %% "zio-test-sbt" % ZioVersion % Test
  lazy val ZioTestMagnolia = ZioGroup %% "zio-test-magnolia" % ZioVersion % Test

}