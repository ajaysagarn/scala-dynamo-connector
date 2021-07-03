name := "chore-crunch"
organization := "com.chore.crunch"
version := "0.0.1"
scalaVersion := "2.13.6"
idePackagePrefix := Some("com.chore.crunch")

val AkkaVersion = "2.6.14"
val AkkaHttpVersion = "10.2.4"

libraryDependencies ++= Seq(
  "com.typesafe.akka" %% "akka-actor-typed" % AkkaVersion,
  "com.typesafe.akka" %% "akka-stream" % AkkaVersion,
  "com.typesafe.akka" %% "akka-http" % AkkaHttpVersion,
  "com.typesafe.akka" %% "akka-http-spray-json" % AkkaHttpVersion,
  "com.typesafe.akka" %% "akka-slf4j" % AkkaVersion,
  "ch.qos.logback" % "logback-classic" % "1.1.2",

  "com.lightbend.akka" %% "akka-stream-alpakka-dynamodb" % "3.0.1",
  "software.amazon.awssdk" % "dynamodb" % "2.11.14"
)


