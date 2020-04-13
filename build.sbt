// TCP Akka Streams Client PoC
//   Inception: 2020-03-06

import Dependencies._
import sbt.Keys.name
//import sbtassembly.AssemblyPlugin.defaultUniversalScript
import sbtassembly.AssemblyPlugin.defaultShellScript


name := """tcom-tier-cli-akka"""

licenses +=("Apache-2.0", url("http://opensource.org/licenses/apache2.0.php"))
licenses := List(
  ("Apache License, Version 2.0",
    url("https://www.apache.org/licenses/LICENSE-2.0"))
)
homepage := Some(url("https://github.com/pragmarad/tcom-tier-cli-akka"))

//--------------------
// Code publish related info:
pomExtra :=
  <scm>
    <connection>
      scm:git:git://github.com/pragmarad/tcom-tier-cli-akka.git
    </connection>
    <url>
      https://github.com/pragmarad/tcom-tier-cli-akka
    </url>
  </scm>
    <developers>
      <developer>
        <id>pragmarad</id>
        <name>PragnmaRAD</name>
        <email>pragmarad.tech@gmail.com</email>
      </developer>
    </developers>

publishTo := Some(
  "bintray" at
    "https://api.bintray.com/maven/pragmarad-tech/" +
      "tcom-scala-akka/tcom-tier-cli-akka;publish=1")
credentials += Credentials(Path.userHome / ".sbt" / ".credentials")
publishMavenStyle := true
// Skip publish sources and/or Javadoc
/*
packagedArtifacts in publish ~= { m =>
  val classifiersToExclude = Set(
    Artifact.SourceClassifier,
    Artifact.DocClassifier
  )
  m.filter { case (art, _) =>
    art.classifier.forall(c => !classifiersToExclude.contains(c))
  }
}
*/

publishArtifact in Test := true // to add the tests JAR
//publishArtifact in Test := false
//--------------------

lazy val commonSettings = Seq(
  organization := "tech.pragmarad.tcom",
  scalaVersion := curScalaVersion,
  // Test options:
  testOptions in Test := Seq(Tests.Argument(TestFrameworks.JUnit, "-a", "-v")),

  //---------
  // Resolvers:
  resolvers ++= ResolverList.projectResolvers,

  //---------
  //---------
  // - Reduce the maximum number of errors shown by the Scala compiler.
  maxErrors := 50,
  // - Increase the time between polling for file changes when using continuous execution.
  //pollInterval := 1000,
  // - Append several options to the list of options passed to the Java compiler.
  //javacOptions in Compile ++= Seq("-source", javaVersion, "-target", javaVersion
  //  , "-Xlint:unchecked", "-Xlint:deprecation", "-g:lines,source,vars"),
  javacOptions in doc in Compile := Seq("-Xdoclint:none"),

  // Disable sbt log buffering so you can enjoy ScalaTest's built-in event buffering algorithm
  logBuffered in Test := false,
  // If no net connection expected, set true temporarily:
  offline := false,
  // - Include Scala version in output paths and artifacts.
  crossPaths := true,
  //---------

  //---------
  // Settings for || execution:
  // - Fork a new JVM for 'run' and 'test:run'
  fork in Runtime := false,
  // - Fork a new JVM for 'test:run', but not 'run'.
  fork in Test := true,
  // - Only use (or not) a single thread for building.
  parallelExecution := false,
  // - Execute tests in the current project serially.
  //   Tests from other projects may still run concurrently.
  parallelExecution in Test := false,
  //---------

  // - Only show warnings and errors on the screen for compilations.
  //   This applies to both test:compile and compile and is Info by default.
  logLevel in compile := Level.Warn,
  // - Only show warnings and errors on the screen for all tasks (the default is Info).
  //   Individual tasks can then be more verbose using the previous setting.
  logLevel := Level.Info,
  // - Only store messages at info and above (the default is Debug).
  //   This is the logging level for replaying logging with 'last'.
  persistLogLevel := Level.Warn,
  // - Only show 10 lines of stack traces
  traceLevel := 10,
  // - Publish test jar, sources, and docs
  publishArtifact in Test := false,
  // Use sbt updateClassifiers
  transitiveClassifiers := Seq("sources", "javadoc")
)

// - Append -deprecation to the options passed to the Scala compiler.
scalacOptions in Compile ++= Seq(
  "-feature",
  "-deprecation",
  "-encoding", "UTF-8",
  "-unchecked",
  "-Xlog-reflective-calls",
  "-Xlint",
  //"-Yno-adapted-args", NOTE: compiler didn't like it..
  "-Ywarn-dead-code",
  "-Ywarn-value-discard",
  //"-Xfuture",
  //"-Xexperimental"
)
scalacOptions in Test += "-Ywarn-value-discard:false" // since this often appears in expectNext(expected) testing style in streams


// Common dep-s:
libraryDependencies in Global ++= Prj.prjDependencies
libraryDependencies in Global ++= Tst.testDependencies

//---------
// Module aggregator:
lazy val root = (project in file("."))
	.settings(commonSettings: _*)
	.settings(
		mainClass in assembly := Some("tech.pragmarad.tcom.client.TcpAkkaStreamClientApp"),
		// Adding SHA-1:
		//assemblyOption in assembly := (assemblyOption in assembly).value.copy(appendContentHash = true),
		// Prepending with shell script:
		assemblyOption in assembly := (assemblyOption in assembly).value.copy(prependShellScript = Some(defaultShellScript))
		// - OR: 
		//assemblyOption in assembly := (assemblyOption in assembly).value.copy(prependShellScript = Some(defaultShellScript)),
        //assemblyJarName in assembly := s"${name.value}-${version.value}"
    )


//---------

// Init:
initialize := {
  //val _ = initialize.value
  val javaVersionOfRun = sys.props("java.specification.version")
  if (javaVersionOfRun.toInt < javaVersion.toInt)
    sys.error(s"Java 11+ is required for this project. But current version is ${javaVersionOfRun}")
}