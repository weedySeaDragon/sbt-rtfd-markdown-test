addSbtPlugin("com.typesafe.sbt" % "sbt-ghpages" % "0.5.4")
addSbtPlugin("com.typesafe.sbt" % "sbt-site" % "1.0.0") // all version >= 0.8
addSbtPlugin("com.github.gseitz" % "sbt-release" % "1.0.0")
addSbtPlugin("com.jsuereth" % "sbt-pgp" % "1.0.0")

libraryDependencies <+= sbtVersion { sv =>
  "org.scala-sbt" % "scripted-plugin" % sv
}

// Scripted plugin needs to declare this as a dependency
libraryDependencies += "jline" % "jline" % "2.11"


// logging uses the SLF4J java logger.  Must add it here so it's avail early. (If included in build.sbt, places that need it can't yet find it.)
// @see https://groups.google.com/d/msg/scalatra-user/Dv0Z-DE-KNM/TaBxBNOG9MwJ
libraryDependencies ++= Seq("ch.qos.logback" % "logback-classic" % "1.1.7",
  "ch.qos.logback" % "logback-core" % "1.1.7")


addSbtPlugin("com.github.jozic" % "sbt-about-plugins" % "0.1.0")
