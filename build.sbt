name := """todo-play-angular-webpack"""

version := "1.0-SNAPSHOT"

lazy val root = (project in file(".")).enablePlugins(PlayJava)

scalaVersion := "2.11.7"

libraryDependencies ++= Seq(
	javaJdbc,
	cache,
	javaWs,
	"com.amazonaws" % "aws-java-sdk" % "1.10.69"
)

PlayKeys.playRunHooks ++= Seq(
	DynamoDB(), Npm()
)

// Disable documentation to speed up compilation
sources in (Compile, doc) := Seq.empty
publishArtifact in (Compile, packageDoc) := false


fork in run := true