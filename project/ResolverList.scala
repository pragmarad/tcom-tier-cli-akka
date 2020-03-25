import sbt._

object ResolverList {
  val projectResolvers = Seq(
    Resolver.defaultLocal,
    Resolver.bintrayRepo("pragmarad-tech", "tcom-scala-akka"),
    "lightbend-maven-releases" at "https://repo.lightbend.com/lightbend/maven-release/",
    Resolver.url("lightbend-ivy-release", url("https://repo.lightbend.com/lightbend/ivy-releases"))(Resolver.ivyStylePatterns),
    Resolver.url("sbt-plugins-releases", url("https://dl.bintray.com/playframework/sbt-plugin-releases"))(Resolver.ivyStylePatterns),
    Resolver.url("Maven Central Repository",url("https://repo1.maven.org/maven2"))(Resolver.ivyStylePatterns),
    "Sonatype" at "https://oss.sonatype.org/content/repositories/releases",
    "Artima Maven Repository" at "https://repo.artima.com/releases"
  )
}

