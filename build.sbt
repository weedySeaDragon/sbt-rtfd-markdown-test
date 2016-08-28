import java.io.{BufferedWriter, FileWriter}
import java.time.Year

import com.typesafe.sbt.site.preprocess._

import sbt.Keys._
import sbtrelease._


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
/*
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
*/

//preprocessVars in Preprocess := Map("VERSION" -> latestVersion)

// Preprocess defaults: includeFilter includes *rst, mapping for vars has VERSION.  So we don't need to set those
//sourceDirectory in Preprocess := sourceDirectory.value / "site-preprocess" / "sphinx"
//target in Preprocess := sourceDirectory.value / "sphinx"

// @see http://blog.byjean.eu/2015/07/10/painless-release-with-sbt.html

//lazy val hello = taskKey[Unit]("Prints 'Hello World'")
//hello := println("hello world!")

//------------------------------------------------------------

import sbtrelease.ReleaseStateTransformations.{setReleaseVersion => _, _}

def setVersionOnly(selectVersion: Versions => String): ReleaseStep = { st: State =>
  val vs = st.get(ReleaseKeys.versions).getOrElse(sys.error("No versions are set! Was this release part executed before inquireVersions?"))
  val selected = selectVersion(vs)

  st.log.info("Setting version to '%s'." format selected)
  val useGlobal = Project.extract(st).get(releaseUseGlobalVersion)
  val versionStr = (if (useGlobal) globalVersionString else versionString) format selected

  reapply(Seq(
    if (useGlobal) version in ThisBuild := selected
    else version := selected
  ), st)
}



// TODO understand Settings!  How to get the settings for the project AND from a plugin
// have to have the current State in order to get the releaseinfo and Project settings
//  @see http://blog.byjean.eu/2015/07/10/painless-release-with-sbt.html example of re-defining the releaseStep for
//    setting the version.  He uses the state to extract info

//-------------------------------------


val dumpInfo = taskKey[File]("Display info in log messages and dump it to a text file. Helpful for learning about sbt.")

dumpInfo := {

  val infoSep = "\n-------------------\n"
  val currentState = state.value

  // TODO get the dump file name
  // writes info to a file.  File = the file we wrote to
  val bd = (baseDirectory in ThisBuild).value / "output.txt"
  // use the FileWriter and a bufferedWriter so we're sure everything gets written out
  val outputFile = new File(bd.getAbsolutePath)
  val bw = new BufferedWriter(new FileWriter(outputFile))

  // info to dump
  //   TODO  one day, load these from the command line.  somehow
  // val info
  // val formatString

  bw.write(s"version in ThisBuild: ${(version in ThisBuild).value} \n\n")
  bw.write(s"$infoSep")

  bw.write(s"\nsettingsData:\n ${settingsData.value.data.mkString("\n  ")}")
  bw.write(s"$infoSep")

  bw.write(s"\nCurrent State: Attributes:\n ${currentState.attributes}")
  bw.write(s"$infoSep")

  bw.write(s"\nCurrent State: Configuration:\n ${currentState.configuration}")
  bw.write(s"$infoSep")

  bw.write(s"\nProject State:\n ${Project.extract(currentState)}")

  bw.close()
  currentState.log.info(s"\nta da!! dumped the info out to the file named:\n   ${outputFile.getAbsolutePath}")
  outputFile  // return the file we wrote to
}


//-----------------------------------------------------------------------------
// WIP to learn how to get the release settings version info  and use it
val getCurrentReleaseVersion = taskKey[String]("get the currentrelease  version info from inquireVersions, return it as a String")

// if this has a State, writing to the log doesn't happen.
// MUST USE m "st.log. "?
getCurrentReleaseVersion := {

  //-----
  //  from ReleaseExtra.scala private[sbtrelease] def setVersion(selectVersion: Versions => String): ReleaseStep =  { st: State =>
  //      lazy val setReleaseVersion: ReleaseStep = setVersion(_._1)

  // from sbt documentation: more about settings

  /* sourceGenerators in Compile += Def.task {
    myGenerator(baseDirectory.value, (managedClasspath in Compile).value)
  }.taskValue
*/

  val currentState = state.value
  //val extractedProjectState = Project.extract(currentState)

  // is this enough to create the dependency we need on the inquireVersions ReleastState task?
  //  this actually *calls* it. hm.  is that what we want?
  val releaseInquireVersionDependency = ReleaseStateTransformations.inquireVersions.apply(currentState)
  //  currentState.log.info(s"releaseInquireVersionDependency: ${releaseInquireVersionDependency.attributes}")

  val releaseVers: Option[(String, String)] = releaseInquireVersionDependency.get(ReleaseKeys.versions)
  val actualVersion: String = releaseVers.get._1 // we know it's the first base on how ReleasePlugin has defined it

  currentState.log.info(s">> HOORAY! The actualVersion = $actualVersion")

  //val selected = selectVersion(vs)
  //st.log.info("Setting version to '%s'." format selected)
  // val useGlobal = st.extract.get(releaseUseGlobalVersion)
  // val versionStr = (if (useGlobal) globalVersionString else versionString) format selected

  //ReleasePlugin.extraReleaseCommands
  //log.info(s"ReleasePlugin.projectSettings: ${ReleasePlugin.projectSettings.mkString(",\n ")}")

  // TODO make this dependent on release-inquire-version or whatever that is

  currentState.log.info(s"version in ThisBuild: ${(version in ThisBuild).value}")

  // writes info to a file.  File = the file we wrote to
  val bd = (baseDirectory in ThisBuild).value / "output.txt"
  val log = streams.value.log
  // FileWriter
  val outputFile = new File(bd.getAbsolutePath)
  val bw = new BufferedWriter(new FileWriter(outputFile))

  // bw.write(s"val vs: vs._1 ${vs._1}    vs._2: ${vs._2}")
  bw.write(s"version in ThisBuild: ${(version in ThisBuild).value} \n\n")
  bw.write(s"releaseInquireVersionDependency: ${releaseInquireVersionDependency.attributes}\n\n")

  val relVars = releaseInquireVersionDependency.get((ReleaseKeys.versions))
  bw.write(s"relVars: ${relVars}\n\n") // HOORAY!!  THIS IS THE INFO!!!


  bw.close()
  log.info(s"ta da!! wrote the output to ${outputFile.getName}")

  actualVersion
}

//---------------------------------------------

val generateSphinxConfigFile = taskKey[File]("Generate the config.py file for Sphinx using preprocess")

generateSphinxConfigFile := {

  val currentState = state.value
  //val extractedProjectState = Project.extract(currentState)  TODO what is the different between this and state.value?

  // get the current release version:
  val currentReleaseVer:String = getCurrentReleaseVersion.value

  // set the Settings for preprocess:
//   val preprocessSphinxConfigInfo := preprocess.PreprocessPlugin.projectSettings

  //preprocess.PreprocessPlugin.autoImport.preprocessVars

  preprocessVars in Preprocess := Map("PROJECT" -> normalizedName.value,
    "VERSION" -> currentReleaseVer,
    "SHORTCOPYRIGHTINFO" -> s"${Year.now()} $mainAuthor",
    "SHORTPROJECTVERSION" -> currentReleaseVer,
    "LONGPROJECTVERSION" -> currentReleaseVer,
    "AUTHORS" -> mainAuthor,
    "MAINAUTHOR" -> mainAuthor,
    "SHORTPROJECTDESC" -> description.value,
    "EPUBPUBLISHER" -> mainAuthor)

  currentState.log.info(s"set version for sphinx to: $currentReleaseVer")

  currentState.log.info(s"preprocessVars: ${(preprocessVars in Preprocess).value.mkString(", ")}")

  new File("output.txt")  // FIXME
}


//---------------------------------------------

val writeVersionIntoSphinxConfig = taskKey[Unit]("Runs preprocess:preprocess to replace template values in sphinx/config.py")
writeVersionIntoSphinxConfig := {

  val log = streams.value.log
  log.warn("A warning from writeVersionIntoSphinxConfig.")

  // get the version from release setReleaseVersion
  val thisVer = ReleaseKeys.versions
  log.info(s" ReleaseKeys.versions= $thisVer")
  println(s" ReleaseKeys.versions= $thisVer")

  val thisVersion = "0.9"
  log.info(s" .... thisVer = $thisVer")
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

  log.info(s"set version for sphinx to: $thisVersion")
  println(s"set version for sphinx to: $thisVersion")


  preprocessIncludeFilter in Preprocess := "*.py"
  preprocessIncludeFilter in Preprocess := (preprocessIncludeFilter in Preprocess).value || "*.rst"

  sourceDirectory in Preprocess := sourceDirectory.value / "site-preprocess" / "sphinx"
  target in Preprocess := baseDirectory.value / "src" / "sphinx" // this is where the preprocessed configuration file needs to be written

  /*
    val result: Option[(State, Result[sbt.File])] = Project.runTask(preprocess in Preprocess, state)

    // handle the result
    val resultingState:State = result match {
       case None => {
         // Key wasn't defined.
         log.error(s"Error when trying to run preprocess: the preprocess task was not defined (Project.runTask(preprocess,state)) resulted in no state; no TaskKey for preprocess was found.")
         state
       }
       case Some((newState, Inc(inc))) => {
         // error detail, inc is of type Incomplete, use Incomplete.show(inc.tpe) to get an error message
        log.error(s"Error when trying to run preprocess: ${Incomplete.show(inc.tpe)}")
         newState
       }
       case Some((newState, Value(v))) => {
         log.info(s"ran preprocess:preprocess to create a sphinx/config.py file with version=$thisVersion")
         // success!
         newState
       }
     }
    */

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
  releaseStepTask(getCurrentReleaseVersion),
  //  runTest,
  // releaseStepInputTask(scripted, " com.typesafe.sbt.packager.universal/* debian/* rpm/* docker/* ash/* jar/* bash/* jdkpackager/*"),
  setReleaseVersion,

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


