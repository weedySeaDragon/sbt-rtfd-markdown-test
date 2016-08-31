import java.io.{BufferedWriter, FileNotFoundException, FileWriter, IOException}
import java.time.{Year, ZonedDateTime}
import java.time.format.DateTimeFormatter

import com.typesafe.sbt.site.preprocess._
import com.typesafe.sbt.site.preprocess.PreprocessPlugin._
import sbt.Keys._
import sbtrelease._

import scala.util.matching.Regex


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
/*
preprocessVars in Preprocess := Map("PROJECT" -> normalizedName.value,
  "VERSION" -> "0.0.0",
  "SHORTCOPYRIGHTINFO" -> s"${Year.now()} $mainAuthor",
  "SHORTPROJECTVERSION" -> "0.0.0",
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
// have to have the current State in order to get the releaseinfo and Project settings
//  @see http://blog.byjean.eu/2015/07/10/painless-release-with-sbt.html example of re-defining the releaseStep for
//    setting the version.  He uses the state to extract info

//-------------------------------------


val dumpInfo = taskKey[File]("Display info in log messages and dump it to a text file. Helpful for learning about sbt.")
dumpInfo := {

  val dtFormatter = DateTimeFormatter.RFC_1123_DATE_TIME
  val infoSep = "\n-------------------\n"
  val infoHeader = "// This file was generated on " + dtFormatter.format(ZonedDateTime.now()) + "\n//\n// Ashley Engelund (weedySeaDragon @ github:  This is information dumped from SBT.\n//  I have a little sbt task that just writes information to a test file.  This is the result.\n//  Each object (information) is separated by this line:" + infoSep + "//  Each object starts with:  <string description>:value used to generate the output:\\n\n\n"

  // TODO get the dump file name
  // writes info to a file.  File = the file we wrote to
  val bd = (baseDirectory in ThisBuild).value / "output.txt"
  // use the FileWriter and a bufferedWriter so we're sure everything gets written out
  val outputFile = new File(bd.getAbsolutePath)
  val bw = new BufferedWriter(new FileWriter(outputFile))
  bw.write(infoHeader)
  bw.write(infoSep)

  // info to dump
  //   TODO  one day, load these from the command line.  somehow
  // val info
  // val formatString
  //  bw.write(infoSep)

  bw.write(s"version in ThisBuild: (version in ThisBuild).value: ${(version in ThisBuild).value} \n\n")
  bw.write(s"$infoSep")

  bw.write(s"\nsettingsData:settingsData.value.data.mkString:\n ${settingsData.value.data.mkString("\n  ")}")
  bw.write(s"$infoSep")


  // currentState > Project state
  //   current state has additional information that may have been set by scope, config, tasks, commands

  val currentState = state.value
  bw.write(s"\nCurrent State: Attributes:currentState.attributes:\n ${currentState.attributes}")
  bw.write(s"$infoSep")

  bw.write(s"\nCurrent State: Configuration:currentState.configuration:\n ${currentState.configuration}")
  bw.write(s"$infoSep")

  bw.write(s"\nProject State:Project.extract(currentState):\n ${Project.extract(currentState)}")

  bw.close()
  currentState.log.info(s"\nta da!! dumped the info out to the file named:\n   ${outputFile.getAbsolutePath}")
  outputFile // return the file we wrote to
}
//---------------

/**
  *
  * *
  * // TODO get current info in a readable way!
  */
/**
  * reminder: settings are immutable, tasks(TaskSettings) are not -- they are evaluated each time they 're called
  * *
  * -- all tasks, all not tasks( = settings)
  * = by scope
  *   - alphabetically
  *   - by plugin / source
  *   - by tuple: project, configuration, scope
  *
  * ex: setting
  * setting (ScopedKey(Scope(Global, Global, Global, Global), initialize)) at LinePosition((sbt.Defaults) Defaults.scala, 156),
  *   - by scope
  *   - then by Source(e.g."Defaults.  Line xx)
  *
  * filter by...
  *   - scope, setting, configuration, project, string, source
  * ex:
  * setting (ScopedKey (Scope(Select(
  * ProjectRef (file :/ Users / ashleyengelund / github / sbt - rtfd - markdown - test /, sbt - rtfd - markdown - test)
  * ),
  * Select (
  * ConfigKey (preprocess)
  * ),
  * Global, Global
  * ),
  * preprocessVars))
  * at LinePosition((com.typesafe.sbt.site.preprocess.PreprocessPlugin) PreprocessPlugin.scala, 31),
  *
  * ex: inTask (compile) (compileInputsSettings)(from Defaults is the
  *
  * supress / hide: (to save space)..project info, source( = linePosition, plugin, etc)
  *
  * AttributeMap
  * entries, get(), contains()
  *
  * AttributeKey
  * - can only get the value via a call to the attributeMap: the unique key is the tuple `(String,T)` (can have 2 different types with same label: String, thus the tuple makes up the unique key)
  * - the value might be delegated.Must use the.extend: method to find those items that it (might) delegate to
  *
  *
  * also examine:
  * Build
  * Defaults
  * State
  * - attributes: AttributesMap
  *
  * from Defaults:
  *
  *
  * *
  *
  * trait BuildExtra extends BuildCommon with DefExtra {
  * import Defaults._
  * ...interesting to see how scopes are referenced in these 2 methods:
  * *
  * def initScoped[T](sk: ScopedKey[_], i: Initialize[T]): Initialize[T] = initScope(fillTaskAxis(sk.scope, sk.key), i)
  * def initScope[T](s: Scope, i: Initialize[T]): Initialize[T] = i mapReferenced Project.mapScope(Scope.replaceThis(s))
  *
  *
  *
  * from ReleaseExtra.scala:
  * getting scoped info from extracted project state:
  * private[sbtrelease]
  * *
  * def resolve[T](key: ScopedKey[T], extracted: Extracted): ScopedKey[T] =
  * Project
  * *
  * .mapScope(Scope.resolveScope(GlobalScope, extracted.currentRef.build, extracted.rootProject))(key.scopedKey)
  *
  *
  *
  * from sbt doc (Build - State.html):
  * All project data is stored in structure.data, which is of type sbt.Settings[Scope].
  * Typically, one gets information of type T in the following way:
  * *
  * val key: SettingKey[T]
  * val scope: Scope
  * val value: Option[T] = key in scope get structure.data
  * *
  * Here, a SettingKey[T] is typically obtained from Keys and is the same type that is used to define settings in
  * .sbt files, for example.Scope selects the scope the key is obtained for.There are convenience overloads of
  * in that can be used to specify only the required scope axes.See Structure.scala for where in and other parts of
  * the settings interface are defined.Some examples:
  * *
  * import Keys._
  * val extracted: Extracted
  * import extracted._
  * *
  * get name of current project
  * val nameOpt: Option[String] = name in currentRef get structure.data
  * *
  * get the package options for the `test:packageSrc` task or Nil if none are defined
  * val pkgOpts: Seq[PackageOption] = packageOptions in(currentRef, Test, packageSrc) get structure.data getOrElse Nil
  *
  *
  *
  *
  */


//-----------------------------------------------------------------------------
// get the release settings version info and return it
val getCurrentReleaseVersion = taskKey[String]("get the currentrelease version info from inquireVersions, return it as a String")
getCurrentReleaseVersion := {
  val currentState = state.value
  // is this enough to create the dependency we need on the inquireVersions ReleastState task?
  //  this actually *calls* it. hm.  is that what we want?
  val releaseInquireVersionDependency = ReleaseStateTransformations.inquireVersions.apply(currentState)
  val releaseVers: Option[(String, String)] = releaseInquireVersionDependency.get(ReleaseKeys.versions)
  val actualVersion: String = releaseVers.get._1 // we know it's the first base on how ReleasePlugin has defined it
  currentState.log.info(s">> HOORAY! The actualVersion = $actualVersion")
  actualVersion
}


//-----------------------------------------------------------------------------

// try to read the version from version.sbt. return Option[String]: Some[String] on success, else
//  example file contents:
// version in ThisBuild := "0.15-SNAPSHOT"
val readVersionFile = taskKey[Option[Version]]("read the version from version.sbt")
readVersionFile := {
  val DefaultVersionFileName = "version.sbt"
  var _versionFileName = DefaultVersionFileName

  val BeforeVersionExtractorRE = """(.*)\"[^\"][\.-a-zA-Z0-9]*\"$""".r.unanchored
  val VersionExtractorRE = Version.VersionR.unanchored

  val versionFN = DefaultVersionFileName // TODO can a task take parameters?
  val log = streams.value.log

  var ver: Option[Version] = None
  try {
    val fileLines = io.Source.fromFile(versionFN).getLines.toList // we are only checking the first line (should have only 1 line)
    if (fileLines.length < 1) log.error(s"The $versionFN is empty. No version was read")
    else {
      // strip out anything in the first line up to the Version info, (quotes are removed, too). Let Version parse the version info
      ver = fileLines.head match {
        case beforeVersion@BeforeVersionExtractorRE(beforeStr) => {
          val verStr = fileLines.head.substring(beforeStr.length + 1, fileLines.head.length - 1) // remove the quotes around the version string
          Version(verStr)
        }
        case _ => None
      }
    }
  } catch {
    case ex: FileNotFoundException => log.error(s"Could not find $versionFN.")
      None
    case ex: IOException => log.error(s"Could not read $versionFN. (IOException)")
      None
  }
  ver
}

//-----------------------------------------------------------------------------
//printValInScope(key: TaskKey[File], scopedIn: Scope) = TaskKey[Unit]("print value of a key in a scope")


//-----------------------------------------------------------------------------
val generateSphinxConfigFile = taskKey[Unit]("Generate the config.py file for Sphinx using preprocess")
generateSphinxConfigFile := {

  // TODO remove/rename existing config.py file

  val currentState = state.value

  //  val currentReleaseVer: String = getCurrentReleaseVersion.value // current version as generated by release
  //  println(s"-- currentReleaseVer: $currentReleaseVer")

  val currentReleaseVer: Option[Version] = readVersionFile.value
  val currentVerStr: String = if (currentReleaseVer != None) currentReleaseVer.get.string else ""

  //  println(s"-- currentRelaseStr: $currentRelaseStr")

  //----------------------------
  // based on code from Alive Alexander: @see http://alvinalexander.com/java/jwarehouse/akka-2.3/project/SphinxDoc.scala.shtml

  def isPyFile(f: File): Boolean = f.getName.endsWith(".py")
  //def isRstFile(f: File): Boolean = f.getName.endsWith(".rst") // not really needed, but here for symmetry

  val sphinxPreprocessFilter: FileFilter = new SimpleFileFilter(isPyFile) || ".rst" // must construct a FileFilter. this is one of the few ways to do it

  // customization of sphinx @<key>@ replacements, add to all sphinx-using projects
  // add additional replacements here


  val sphinxTemplateValues = Map("PROJECT" -> normalizedName.value,
    "VERSION" -> currentVerStr,
    "SHORTCOPYRIGHTINFO" -> s"${Year.now()} $mainAuthor",
    "SHORTPROJECTVERSION" -> currentVerStr,
    "LONGPROJECTVERSION" -> currentVerStr,
    "AUTHORS" -> mainAuthor,
    "MAINAUTHOR" -> mainAuthor,
    "SHORTPROJECTDESC" -> description.value,
    "EPUBPUBLISHER" -> mainAuthor)

  // pre-processing settings for generating a sphinx config.py file
  /**
    * Need to have these settings set to what you want:
    * (all in Preprocess):
    * sourceDirectory,  // default = src/site-preprocess
    * target,
    * preprocessIncludeFilter,
    * preprocessVars.
    */

  // Settings are VALS so are not updated (unless sbt is reloaded)
  // NOTE: these have to be ALREADY INITIALIZED before this task is called.  that's why the exist in the project definition
  // Thus they can't be changed here.
  // TODO figure out how to address this.
  /**
    * A setting can be scoped in three axes (project, configuration, and task)
    * we have just this 1 project and so can't change that axis
    * but we can work with configuration and task
    */
  target in Preprocess := sourceDirectory.value / "sphinx"
  preprocessIncludeFilter in Preprocess := sphinxPreprocessFilter
  preprocessVars in Preprocess <<= Def.setting(Map("PROJECT" -> normalizedName.value,
    "VERSION" -> currentVerStr,
    "SHORTCOPYRIGHTINFO" -> s"${Year.now()} $mainAuthor",
    "SHORTPROJECTVERSION" -> currentVerStr,
    "LONGPROJECTVERSION" -> currentVerStr,
    "AUTHORS" -> mainAuthor,
    "MAINAUTHOR" -> mainAuthor,
    "SHORTPROJECTDESC" -> description.value,
    "EPUBPUBLISHER" -> mainAuthor))
  //  cleanFiles <+= target in preprocess in Sphinx

  currentState.log.info(s"set version for sphinx to: $currentVerStr")
  currentState.log.info("Preprocess settings:")
  currentState.log.info(s"  target in Preprocess: ${(target in Preprocess).value}")
  currentState.log.info(s"  preprocesIncludeFilter: ${(preprocessIncludeFilter in Preprocess).value}")
  currentState.log.info(s"  preprocessVars: ${(preprocessVars in Preprocess).value.mkString(", ")}")
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
  /*
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
  */
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

//generateSphinxConfigFile <<= generateSphinxConfigFile dependsOn getCurrentReleaseVersion
preprocess in Preprocess <<= preprocess in Preprocess dependsOn generateSphinxConfigFile
generateHtml in Sphinx <<= generateHtml in Sphinx dependsOn (preprocess in Preprocess)


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

lazy val psk = SettingKey[Unit]("psk", "Print Scoped Setting Key")
val pskSetting = psk := println(" ** psk " + name.value)

lazy val pskT = TaskKey[Unit]("psk", "Print Scoped Task Key")
val pskTSetting = pskT := {
  val thisPsk = psk.value
  println(" ** psk Task " + name.value)
}


lazy val showPsks = taskKey[Unit]("print psks in ALL scopes")
showPsks := {
  // val allTasksAndSettings = BuiltinCommands.allTaskAndSettingKeys(state.value)
  // val allSettings = BuiltinCommands.allTaskAndSettingKeys(state.value).filter(key => !BuiltinCommands.isTask(key.manifest))

  def configLessThan(c1: Configuration, c2: Configuration) = (c1.name < c2.name)
  val projectConfigurations = (ivyConfigurations.value ++ Project.project.configurations).sortWith(configLessThan)
  println(s" All Possible Configurations: ${projectConfigurations.mkString(", ")}")

  //  val extractedState =  Project.extract(state.value)
  //  val extratedBuildStructure = Project.structure(state.value)

  // get the key for all known configurations, including Global:
  // Seq(...) must be Def.Setting[ ] (not AttributeKey). That's why we use pskSetting & pskTSetting instead of psk and pskTask
  val myPsks = projectConfigurations.flatMap { config => inConfig(config)(Seq(pskSetting, pskTSetting)) }.distinct

  //println(s" extractedState = ${extractedState}")
  println(s"\n *** psk in all scopes (Configuration, Task, Project, Extras) tuple:\n")

  myPsks.foreach((currPsk:Def.Setting[_]) => {
    val desc = currPsk.key.key.description.getOrElse("")
    val currScope = currPsk.key.scope
    val currConfigKey:ConfigKey = currScope.config.toOption.get

    println(s" this key: ${currPsk.key.key} label: ${currPsk.key.key.label} desc: $desc")
    println(s"   Scope: config: ${currConfigKey.name}, task: ${currScope.task}, project: ${currScope.project}, extra key(s): ${currScope.extra} ")
    val displaySepString = ""
    println(s"    scope is displayed as ${Scope.display(currScope, displaySepString)}")
    println(s"  defined at this source position: ${currPsk.pos}")
    println("")
  })

}


