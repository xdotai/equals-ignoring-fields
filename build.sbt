name := "x.equals"
version := "1.0"
scalaVersion := "2.11.8"

libraryDependencies ++= Seq(
  "com.chuusai" %% "shapeless" % "2.3.1",
  "org.scalactic" %% "scalactic" % "3.0.0-RC4",
  "org.scalatest" %% "scalatest" % "3.0.0-RC4" % "test",
"com.lihaoyi" % "ammonite-repl" % "0.6.2" cross CrossVersion.full
)



scalacOptions := Seq(
  "-feature",
  "-language:higherKinds",
  "-language:implicitConversions",
  "-unchecked"
//  "-Xlog-implicits"
)

