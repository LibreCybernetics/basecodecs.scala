ThisBuild / libraryDependencySchemes += "org.scala-native" % "sbt-scala-native" % "always"

addSbtPlugin("org.portable-scala" % "sbt-scalajs-crossproject"      % "1.2.0")
addSbtPlugin("org.portable-scala" % "sbt-scala-native-crossproject" % "1.2.0")
addSbtPlugin("org.scala-native"   % "sbt-scala-native"              % "0.4.10")
addSbtPlugin("org.scala-js"       % "sbt-scalajs"                   % "1.13.0")

addSbtPlugin("org.wartremover" % "sbt-wartremover" % "3.0.9")

addSbtPlugin("org.scoverage" % "sbt-scoverage" % "2.0.7")
