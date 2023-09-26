import sbt.Project.projectToRef
import sbtcrossproject.CrossProject

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
  resolvers    :=
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
  crossProject(JVMPlatform, NativePlatform, JSPlatform)
    .crossType(CrossType.Pure)
    .in(file("core"))
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
  crossProject(JVMPlatform)
    .crossType(CrossType.Pure)
    .in(file("core/bench"))
    .dependsOn(core)
    .settings(sharedSettings)
    .settings(
      githubWorkflowArtifactUpload := false,
      libraryDependencies ++= Seq(
        "commons-codec" % "commons-codec" % Version.commonsCodec
      )
    )
    .enablePlugins(JmhPlugin)

val root: CrossProject =
  crossProject(JVMPlatform, NativePlatform, JSPlatform)
    .crossType(CrossType.Pure)
    .in(file("fake"))
    .aggregate(core)
    .dependsOn(core)
    .enablePlugins(ScalaUnidocPlugin)
    .settings(sharedSettings)
    .settings(
      name                                       := "basecodecs",
      ScalaUnidoc / unidoc / unidocProjectFilter := inProjects(thisProject.value.aggregate*)
    )

// To avoid publishing the default root package
val fakeRoot = (project in file("."))
  .settings(publish / skip := true)
  .aggregate(root.componentProjects.map(projectToRef)*)

// CI/CD

import JavaSpec.Distribution

ThisBuild / githubWorkflowJavaVersions := Seq(
  JavaSpec(Distribution.Zulu, "17"),
  JavaSpec(Distribution.Zulu, "21"),
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
