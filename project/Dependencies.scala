import sbt._

object Dependencies {
  val javaVersion = "11"
  val curScalaVersion = "2.13.1"

  //----------
  // Project related libs:
  object Prj {
    val tcomProjectGroup = "tech.pragmarad.tcom"
    val tcomCommonsVersion = "0.1.1"
    val tcomCommons = tcomProjectGroup %% "tcom-crosscut-commons-akka" % tcomCommonsVersion
    val prjDependencies: Seq[ModuleID] = Seq(tcomCommons)
  }
  //----------

  //----------
  // Test libs:
  object Tst {
    //----
    val scalaTestVersion = "3.1.1"
    val scalaTest = "org.scalatest" %% "scalatest" % scalaTestVersion % Test

    val scalaCheckVersion = "1.14.0"
    val scalaCheck = "org.scalacheck" %% "scalacheck" % scalaCheckVersion % Test

    lazy val testDependencies: Seq[ModuleID] = Seq(scalaTest, scalaCheck)
  }

  //----------

}