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
    url("https://github.com/LibreCybernetics/rfc4648.scala"),
    "scm:git@github.com:LibreCybernetics/rfc4648.scala.git"
  )
)

ThisBuild / versionScheme := Some("strict")
ThisBuild / scalaVersion  := Version.scala

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
  resolvers    := Seq(
    Resolver.mavenLocal,
    "Jitpack" at "https://jitpack.io",
    "GitHub Package Registry" at "https://maven.pkg.github.com/LibreCybernetics/rfc4648.scala"
  ),
  publishTo    := Some(
    "GitHub Package Registry" at "https://maven.pkg.github.com/LibreCybernetics/rfc4648.scala"
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

wartremoverErrors ++= Warts.unsafe

val core =
  crossProject(JVMPlatform, NativePlatform, JSPlatform)
    .crossType(CrossType.Pure)
    .in(file("core"))
    .settings(sharedSettings)
    .settings(
      name := "rfc4648-core",
      libraryDependencies ++= Seq(
        "dev.librecybernetics.bijection~scala" %%% "bijection-core"     % Version.bijection,
        "org.scalatest"                        %%% "scalatest"          % Version.scalatest          % Test,
        "org.scalatest"                        %%% "scalatest-wordspec" % Version.scalatest          % Test,
        "org.scalatestplus"                    %%% "scalacheck-1-17"    % Version.scalatestPlusCheck % Test
      )
    )

val root: CrossProject =
  crossProject(JVMPlatform, NativePlatform, JSPlatform)
    .crossType(CrossType.Pure)
    .in(file("."))
    .aggregate(core)
    .dependsOn(core)
    .enablePlugins(ScalaUnidocPlugin)
    .settings(sharedSettings)
    .settings(
      name                                       := "rfc4648",
      ScalaUnidoc / unidoc / unidocProjectFilter := inProjects(thisProject.value.aggregate*)
    )

// To avoid publishing the default root package / `rfc4648-scala`

val fakeRoot = (project in file("."))
  .settings(
    publish / skip  := true,
    sourceDirectory := file("fake")
  )
  .aggregate(root.componentProjects.map(projectToRef)*)

// CI/CD

ThisBuild / githubWorkflowJavaVersions := Seq(
  JavaSpec.temurin("11"),
  JavaSpec.temurin("17"),
  JavaSpec.temurin("20")
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