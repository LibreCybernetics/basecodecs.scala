// Globals

ThisBuild / organization := "dev.librecybernetics"
ThisBuild / licenses     := Seq(
  "LicenseRef-Anti-Capitalist Software License-1.4" -> url("https://anticapitalist.software/"),
  "LicenseRef-Cooperative Softawre License"         -> url("https://lynnesbian.space/csl/formatted/"),
  "Parity-7.0.0"                                    -> url("https://paritylicense.com/versions/7.0.0")
)

ThisBuild / scmInfo := Some(
  ScmInfo(
    url("https://github.com/LibreCybernetics/basecodecs.scala"),
    "scm:git@github.com:LibreCybernetics/basecodecs.scala.git"
  )
)

ThisBuild / versionScheme := Some("strict")
ThisBuild / scalaVersion  := Version.scala

resolvers ++= Resolver.sonatypeOssRepos("snapshots")

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
    "-language:captureChecking",
    "-language:implicitConversions",
    "-language:saferExceptions",
    "-Ykind-projector:underscores",
    "-Ysafe-init",
    "-Xfatal-warnings"
  ),
  resolvers    := Resolver.sonatypeOssRepos("snapshots") ++
    Seq(
      Resolver.mavenLocal,
      "Jitpack" at "https://jitpack.io",
      "GitHub Package Registry" at "https://maven.pkg.github.com/LibreCybernetics/basecodecs.scala"
    ),
  publishTo    := Some(
    "GitHub Package Registry" at "https://maven.pkg.github.com/LibreCybernetics/basecodecs.scala"
  ),
  credentials  := Seq(
    Credentials(
      "GitHub Package Registry",
      "maven.pkg.github.com",
      "LibreCybernetics",
      sys.env.getOrElse("GITHUB_TOKEN", "")
    )
  )
)

ThisBuild / wartremoverErrors ++= Warts.unsafe

val core =
  (projectMatrix in file("core"))
    .jsPlatform(scalaVersions = Seq(Version.scala))
    .jvmPlatform(scalaVersions = Seq(Version.scala))
    .nativePlatform(scalaVersions = Seq(Version.scala))
    .settings(sharedSettings)
    .settings(
      name := "basecodecs-core",
      libraryDependencies ++= Seq(
        "dev.librecybernetics.bijection~scala" %%% "bijection-core"      % Version.bijection,
        "dev.librecybernetics.bijection~scala" %%% "bijection-scalatest" % Version.bijection          % Test,
        "org.scalatest"                        %%% "scalatest"           % Version.scalatest          % Test,
        "org.scalatest"                        %%% "scalatest-wordspec"  % Version.scalatest          % Test,
        "org.scalatestplus"                    %%% "scalacheck-1-17"     % Version.scalatestPlusCheck % Test
      )
    )

val `core-bench` =
  (projectMatrix in file("core/bench"))
    .jsPlatform(scalaVersions = Seq(Version.scala))
    .jvmPlatform(scalaVersions = Seq(Version.scala))
    .nativePlatform(scalaVersions = Seq(Version.scala))
    .dependsOn(core)
    .settings(sharedSettings)
    .settings(
      githubWorkflowArtifactUpload := false,
      libraryDependencies ++= Seq(
        "commons-codec" % "commons-codec" % Version.commonsCodec
      )
    )
    .enablePlugins(JmhPlugin)

val rootMatrix =
  (projectMatrix in file("."))
    .jsPlatform(scalaVersions = Seq(Version.scala))
    .jvmPlatform(scalaVersions = Seq(Version.scala))
    .nativePlatform(scalaVersions = Seq(Version.scala))
    .aggregate(core)
    .dependsOn(core)
    .settings(sharedSettings)
    .settings(name := "basecodecs")
    .enablePlugins(ScalaUnidocPlugin)
    .settings(
      ScalaUnidoc / unidoc / unidocProjectFilter := inProjects(thisProject.value.aggregate*)
    )

val root =
  (project in file("."))
    .aggregate(rootMatrix.projectRefs*)
    .settings(sharedSettings)
    .settings(publish / skip := true)

// CI/CD

import JavaSpec.Distribution.Zulu

ThisBuild / githubWorkflowJavaVersions := Seq(
  JavaSpec(Zulu, "17"),
  JavaSpec(Zulu, "21")
)

ThisBuild / githubWorkflowTargetTags :=
  Seq("v*")

ThisBuild / githubWorkflowPublishTargetBranches :=
  Seq(
    RefPredicate.StartsWith(Ref.Tag("v")),
    RefPredicate.Equals(Ref.Branch("main"))
  )

ThisBuild / githubWorkflowPublish := Seq(
  WorkflowStep.Sbt(
    commands = List("ci-release"),
    name = Some("Publish project"),
    env = Map(
      "CI_CLEAN"            -> "; clean",
      "CI_SONATYPE_RELEASE" -> "version", // Not configure, skip
      "GITHUB_TOKEN"        -> "${{ secrets.GITHUB_TOKEN }}",
      "PGP_PASSPHRASE"      -> "${{ secrets.PGP_PASSPHRASE }}",
      "PGP_SECRET"          -> "${{ secrets.PGP_SECRET }}"
    )
  )
)
