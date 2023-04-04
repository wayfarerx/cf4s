import sbt.*
object Dependencies {

  val AwsGroup = "com.amazonaws"
  val AwsVersion = "1.12.429"
  lazy val AwsCore = AwsGroup % "aws-java-sdk-core" % AwsVersion

  val CirceGroup = "io.circe"
  val CirceVersion = "0.14.5"
  lazy val CirceCore = CirceGroup %% "circe-core" % CirceVersion
  lazy val CirceParser = CirceGroup %% "circe-parser" % CirceVersion

  lazy val Scopt = "com.github.scopt" %% "scopt" % "4.1.0"

  val ZioGroup = "dev.zio"
  val ZioVersion = "2.0.10"
  lazy val Zio = ZioGroup %% "zio" % ZioVersion
  lazy val ZioInteropCats = ZioGroup %% "zio-interop-cats" % "23.0.0.1"
  lazy val ZioLogging = ZioGroup %% "zio-logging" % "2.1.11"
  lazy val ZioTest = ZioGroup %% "zio-test" % ZioVersion % Test
  lazy val ZioTestSbt = ZioGroup %% "zio-test-sbt" % ZioVersion % Test
  lazy val ZioTestMagnolia = ZioGroup %% "zio-test-magnolia" % ZioVersion % Test

}