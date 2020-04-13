// SBT plugins:

// See https://github.com/sbt/sbt-release
// Usage: sbt release
addSbtPlugin("com.github.gseitz" % "sbt-release" % "1.0.13")

// See https://github.com/sbt/sbt-git
addSbtPlugin("com.typesafe.sbt" % "sbt-git" % "1.0.0")

// NOTE sbt-autoversion uses sbt-release and sbt-git
// See https://github.com/sbt/sbt-autoversion
addSbtPlugin("org.scala-sbt" % "sbt-autoversion" % "1.0.0")

// See https://github.com/sbt/sbt-pgp
/* NOTE: before use install gpg. Its version can be checked using:
gpg --version
  - e.g.: gpg (GnuPG) 2.2.4  libgcrypt 1.8.1 etc...

You should also have a program named gpg-agent running in the background.
$ ps aux | grep gpg
*/
addSbtPlugin("com.jsuereth" % "sbt-pgp" % "2.0.0")

// See https://github.com/sbt/sbt-assembly
addSbtPlugin("com.eed3si9n" % "sbt-assembly" % "0.14.10")
