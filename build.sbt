import java.time.Year

import sbt.Keys._

import org.slf4j.Logger
import org.slf4j.LoggerFactory


name := "sbt-rtfd-markdown-test"
organization := "com.ashleycaroline"
val author = "ashley engelund (weedy-sea-dragon @ github)"

version := "0.0.1a"

scalaVersion := "2.11.8"

//scalaVersion in Global := "2.10.5"
//scalacOptions in Compile ++= Seq("-deprecation", "-target:jvm-1.7")


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


// TODO get the version from the version.sbt file
// show thisProject

preprocessVars in Preprocess := Map("PROJECT" -> normalizedName.value,
  "VERSION" -> releaseVersion.value.toString(),
  "SHORTCOPYRIGHTINFO" -> s"${Year.now()} $mainAuthor",
  "SHORTPROJECTVERSION" -> releaseVersion.value.toString(),
  "LONGPROJECTVERSION" -> version.value,
  "AUTHORS" -> mainAuthor,
  "MAINAUTHOR" -> mainAuthor,
  "SHORTPROJECTDESC" -> description.value,
  "EPUBPUBLISHER" -> mainAuthor)

preprocessIncludeFilter in Preprocess := "*.py"
preprocessIncludeFilter in Preprocess := (preprocessIncludeFilter in Preprocess).value || "*.rst"

target in Preprocess := baseDirectory.value / "src" / "sphinx"
// this is where the preprocessed configuration file needs to be written


//preprocessVars in Preprocess := Map("VERSION" -> latestVersion)

// Preprocess defaults: includeFilter includes *rst, mapping for vars has VERSION.  So we don't need to set those
//sourceDirectory in Preprocess := sourceDirectory.value / "site-preprocess" / "sphinx"
//target in Preprocess := sourceDirectory.value / "sphinx"

// @see http://blog.byjean.eu/2015/07/10/painless-release-with-sbt.html

/*
slf4j-api.jar and logback-core.jar in addition to logback-classic.jar on the classpath.
 */


//lazy val hello = taskKey[Unit]("Prints 'Hello World'")
//hello := println("hello world!")


val writeVersionIntoSphinxConfig = taskKey[Unit]("Runs preprocess:preprocess to replace template values in sphinx/config.py")
writeVersionIntoSphinxConfig := { state: State =>

 // val s: TaskStreams = streams.value // enable logging for this ERROR causes a compile problem
 //orig: val log = streams.value.log

  logBuffered := false


  //val log = streams.value.log
 state.log.warn("A warning from writeVersionIntoSphinxConfig.")
  // get the version from release setReleaseVersion

  val thisVer = ReleaseKeys.versions

  state.log.info(s" ReleaseKeys.versions= $thisVer")
  println(s" ReleaseKeys.versions= $thisVer")

  val thisVersion = "0.9"
  //val vs = get(ReleaseKeys.versions).getOrElse(sys.error("No versions are set! Was this release part executed before inquireVersions?"))
  //val thisVersion = vs._1 // the current version as defined by release

  // now preprocess:
  preprocessVars in Preprocess := Map("PROJECT" -> normalizedName.value,
    "VERSION" -> thisVersion,
    "SHORTCOPYRIGHTINFO" -> s"${Year.now()} $mainAuthor",
    "SHORTPROJECTVERSION" -> thisVersion,
    "LONGPROJECTVERSION" -> thisVersion,
    "AUTHORS" -> mainAuthor,
    "MAINAUTHOR" -> mainAuthor,
    "SHORTPROJECTDESC" -> description.value,
    "EPUBPUBLISHER" -> mainAuthor)

  state.log.info(s"set version for sphinx to: $thisVersion")
  println(s"set version for sphinx to: $thisVersion")


 preprocessIncludeFilter in Preprocess := "*.py"
  preprocessIncludeFilter in Preprocess := (preprocessIncludeFilter in Preprocess).value || "*.rst"

  target in Preprocess := baseDirectory.value / "src" / "sphinx" // this is where the preprocessed configuration file needs to be written

  val result: Option[(State, Result[sbt.File])] = Project.runTask(preprocess, state)

  // handle the result
  val resultingState:State = result match {
     case None => {
       // Key wasn't defined.
       state.log.error(s"Error when trying to run preprocess: the preprocess task was not defined (Project.runTask(preprocess,state)) resulted in no state; no TaskKey for preprocess was found.")
       state
     }
     case Some((newState, Inc(inc))) => {
       // error detail, inc is of type Incomplete, use Incomplete.show(inc.tpe) to get an error message
      state.log.error(s"Error when trying to run preprocess: ${Incomplete.show(inc.tpe)}")
       newState
     }
     case Some((newState, Value(v))) => {
       state.log.info(s"ran preprocess:preprocess to create a sphinx/config.py file with version=$thisVersion")
       // success!
       newState
     }
   }

   resultingState
}



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
  //  runTest,
  // releaseStepInputTask(scripted, " com.typesafe.sbt.packager.universal/* debian/* rpm/* docker/* ash/* jar/* bash/* jdkpackager/*"),
  setReleaseVersion,
  releaseStepTask( writeVersionIntoSphinxConfig),
  commitReleaseVersion,
  tagRelease,

  //  publishArtifacts,
  setNextVersion,
  commitNextVersion,
  //pushChanges,
  releaseStepTask(GhPagesKeys.pushSite)
)

// ReadTheDocs will detect that a commit has been made to the github repository and will then
//    automatically build the documentation


