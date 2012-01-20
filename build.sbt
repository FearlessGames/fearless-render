/** Project */
name := "fearless-render"

version := "0.0.1"

organization := "se.fearlessgames"

scalaVersion := "2.9.1"

crossScalaVersions := Seq("2.9.0")

/** Dependencies */
resolvers ++= Seq("snapshots-repo" at "http://scala-tools.org/repo-snapshots")

libraryDependencies <<= scalaVersion { scala_version => Seq(
  "junit" % "junit" % "4.10",
  "com.google.guava" % "guava" % "11.0",
  "org.lwjgl.lwjgl" % "lwjgl" % "2.8.2",
  "org.lwjgl.lwjgl" % "lwjgl_util" % "2.8.2",
  "se.mockachino" % "mockachino" % "0.6.0"
  )
}

javaOptions in run += "-Djava.library.path=target/natives"

