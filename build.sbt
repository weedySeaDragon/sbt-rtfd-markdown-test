name := "sbt-rtfd-markdown-test"
organization := "com.ashleycaroline"
val author = "ashley engelund (weedy-sea-dragon @ github)"

version := "1.0"
//scalaVersion := "2.11.8"


// configure github page
enablePlugins(SphinxPlugin, SiteScaladocPlugin, PreprocessPlugin)

// Preprocess settings for generating documentation:


//preprocessVars in Preprocess := Map("VERSION" -> latestVersion)

// Preprocess defaults: includeFilter includes *rst, mapping for vars has VERSION.  So we don't need to set those
sourceDirectory in Preprocess := sourceDirectory.value / "site-preprocess" / "sphinx"
target in Preprocess := sourceDirectory.value / "sphinx"

//------------------------
//  Github pages settings
ghpages.settings
git.remoteRepo := "git@github.com:weedy-sea-dragon/sbt-native-packager.git"

//-------------------------
// Release process and info (sbt-release plugin)

// Release configuration
//releasePublishArtifactsAction := PgpKeys.publishSigned.value
publishMavenStyle := false

import ReleaseTransformations._
releaseProcess := Seq[ReleaseStep](
  checkSnapshotDependencies,
  inquireVersions,
  runTest,
 // releaseStepInputTask(scripted, " com.typesafe.sbt.packager.universal/* debian/* rpm/* docker/* ash/* jar/* bash/* jdkpackager/*"),
  setReleaseVersion,
  commitReleaseVersion,
  tagRelease,
  publishArtifacts,
  setNextVersion,
  commitNextVersion,
  pushChanges,
  releaseStepTask(GhPagesKeys.pushSite)
)

// ReadTheDocs will detect that a commit has been made to the github repository and will then
//    automatically build the documentation


