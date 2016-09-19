import sbt.Keys._
import sbt._

import scala.util.Properties._

object ApplicationBuild extends Build {

  val appName = "loan-api"
  val appVersion = "0.1.0"

  val main = play.Project(appName, appVersion)
    .settings(libraryDependencies ++= Seq("org.scalatestplus" % "play_2.10" % "1.0.0" % "test"))

}