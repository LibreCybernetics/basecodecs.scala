// Globals

ThisBuild / organization := "dev.librecybernetics"
ThisBuild / licenses     := Seq(
  "LicenseRef-Anti-Capitalist Software License-1.4" -> url("https://anticapitalist.software/"),
  "LicenseRef-Cooperative Softawre License"         -> url("https://lynnesbian.space/csl/formatted/"),
  "Parity-7.0.0"                                    -> url("https://paritylicense.com/versions/7.0.0")
)

ThisBuild / scmInfo := Some(
  ScmInfo(
    url("https://github.com/LibreCybernetics/rfc4648.scala"),
    "scm:git@github.com:LibreCybernetics/rfc4648.scala.git"
  )
)

ThisBuild / versionScheme     := Some("semver-spec")
ThisBuild / scalaVersion      := Version.scala
Global / onChangedBuildSource := ReloadOnSourceChanges

ThisBuild / resolvers := Seq(
  Resolver.mavenLocal,
  "Jitpack" at "https://jitpack.io",
  "GitHub Package Registry" at "https://maven.pkg.github.com/LibreCybernetics/rfc4648.scala",
)
Global / credentials += Credentials(
  "Github Package Registry",
  "maven.pkg.github.com",
  "LibreCybernetics",
  sys.env.getOrElse("GITHUB_TOKEN", "")
)

val sharedSettings = Seq(
  scalaVersion := Version.scala,
  scalacOptions ++= Seq(
    "-explain",
    "-explain-types",
    // Extra Warnings
    "-deprecation",
    "-feature",
    "-unchecked",
    // Extra flags
    "-language:implicitConversions",
    "-Ykind-projector:underscores",
    "-Xfatal-warnings"
  ),
)

wartremoverErrors ++= Warts.unsafe

//=================//
// Upstream issues //
//=================//

// Scoverage of JS/Native
// - https://github.com/lampepfl/dotty/issues/15383
// - https://github.com/lampepfl/dotty/issues/16124
// ("org.scoverage" %%% "scalac-scoverage-runtime" % "2.0.8" % Test).cross(CrossVersion.for3Use2_13)

//=========//
// Modules //
//=========//

val core =
  crossProject(JVMPlatform, NativePlatform, JSPlatform)
    .crossType(CrossType.Pure)
    .in(file("core"))
    .settings(sharedSettings)
    .settings(
      name := "rfc4648",
      libraryDependencies ++= Seq(
        "dev.librecybernetics.bijection~scala" %%% "bijection-core" % Version.bijection,
        "org.scalatest"     %%% "scalatest"          % Version.scalatest          % Test,
        "org.scalatest"     %%% "scalatest-wordspec" % Version.scalatest          % Test,
        "org.scalatestplus" %%% "scalacheck-1-17"    % Version.scalatestPlusCheck % Test
      )
    )
