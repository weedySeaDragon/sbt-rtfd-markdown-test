import java.time.Year

name := "sbt-rtfd-markdown-test"
organization := "com.ashleycaroline"
val author = "ashley engelund (weedy-sea-dragon @ github)"

version := "1.0"

//scalaVersion := "2.11.8"


// configure github page
enablePlugins(SphinxPlugin, SiteScaladocPlugin, PreprocessPlugin)

//--------------------------------------------------
// Preprocess settings for generating documentation:


resourceDirectory := baseDirectory.value / "resources"

/*
# @PROJECT@              project name ex: sbt-native-packager
# @SHORTCOPYRIGHTINFO@   short copyright info ex: 2016 sbt-native-packager team (whatever you want to come after "Copyright ")
# @SHORTPROJECTVERSION@  the short X.Y version of the project ex: 1.0
# @LONGPROJECTVERSION@   the long version of the project, including alpha, beta tags, etc. ex: 1.0-ALPHA
# @AUTHORS@              authors on the project
# @MAINAUTHOR@           one (main or original) author for places where only one can be listed ex: Josh Suereth
# @SHORTPROJECTDESC@     a short (one line) description of the project
# @EPUBPUBLISHER@        publisher of the ePub version of this documentation ex: Josh Suereth
 */
// vars and values for the sphinx config file
val mainAuthor = author


// TODO get the version from... github? from the version.sbt file?

preprocessVars in Preprocess := Map("PROJECT" -> normalizedName.value,
  "SHORTCOPYRIGHTINFO" -> s"${Year.now()} $mainAuthor",
  "SHORTPROJECTVERSION" -> version.value,
  "LONGPROJECTVERSION" -> version.value,
  "AUTHORS" -> mainAuthor,
  "MAINAUTHOR" -> mainAuthor,
  "SHORTPROJECTDESC" -> description.value,
  "EPUBPUBLISHER" -> mainAuthor)

preprocessIncludeFilter in Preprocess := "*.py"

target in Preprocess := baseDirectory.value / "src" / "sphinx"
// this is where the preprocessed configuration file needs to be written


//preprocessVars in Preprocess := Map("VERSION" -> latestVersion)

// Preprocess defaults: includeFilter includes *rst, mapping for vars has VERSION.  So we don't need to set those
//sourceDirectory in Preprocess := sourceDirectory.value / "site-preprocess" / "sphinx"
//target in Preprocess := sourceDirectory.value / "sphinx"




//------------------------
//  Github pages settings
ghpages.settings
git.remoteRepo := "git@github.com:weedy-sea-dragon/sbt-native-packager.git"



//-------------------------
// Release process and info (sbt-release plugin)


// Release configuration

//releasePublishArtifactsAction := PgpKeys.publishSigned.value

publishMavenStyle := false

import java.time.Year

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


