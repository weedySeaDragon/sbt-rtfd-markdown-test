import java.io.{BufferedWriter, FileNotFoundException, FileWriter, IOException}
import java.time.{Year, ZonedDateTime}
import java.time.format.DateTimeFormatter

import com.typesafe.sbt.site.preprocess._
import com.typesafe.sbt.site.preprocess.PreprocessPlugin._
import sbt.Keys._
import sbt.complete.DefaultParsers._
import sbt.complete.Parser
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


resourceDirectory := baseDirectory.value / "resources"

val mainAuthor = author


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
  * TODO get current info in a readable way!
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
  log.info(ver match {
    case None => "Unable to read version.sbt"
    case Some(v) => s"$versionFN has this version: ${v.string}"
  })

  ver
}


//---------------------------------------------

//generateSphinxConfigFile <<= generateSphinxConfigFile dependsOn getCurrentReleaseVersion
//preprocess in Preprocess <<= preprocess in Preprocess dependsOn generateSphinxConfigFile
//generateHtml in Sphinx <<= generateHtml in Sphinx dependsOn (preprocess in Preprocess)


//-------------------------------------------
//  will this overwrite an existing config.py file?  should be an option?
def generateSphinxConfigAction(state: State): State = {
  val extracted = Project extract state
  val log = state.log

  // get the version info from the version.sbt file
  val (readState, versionFromFile) = extracted.runTask(readVersionFile, state)

  versionFromFile match {
    case None => {
      // bad news.  we couldn't get the version.  tell the user and end here.
      log.error("Could not read the version from the version.sbt file")
      log.error("Cannot create a sphinx config.py file without the info from version.sbt.")
      log.error("No config.py file will be generated.")
      state.fail // we didn't change the state; return the one we started with
    }
    case Some(versionInfo) => {
      val currentVerStr = versionInfo.string // version info from the version.sbt file

      def isPyFile(f: File): Boolean = f.getName.endsWith(".py")
      //def isRstFile(f: File): Boolean = f.getName.endsWith(".rst") // not really needed, but here for symmetry
      val sphinxPreprocessFilter: FileFilter = new SimpleFileFilter(isPyFile) || ".rst" // must construct a FileFilter. this is one of the few ways to do it

      val newTarget = target in Preprocess := sourceDirectory.value / "sphinx"
      val newIncludeFilter = preprocessIncludeFilter in Preprocess := sphinxPreprocessFilter
      val newPreproVars = preprocessVars in Preprocess := Map("PROJECT" -> normalizedName.value,
        "VERSION" -> currentVerStr,
        "SHORTCOPYRIGHTINFO" -> s"${Year.now()} $mainAuthor",
        "SHORTPROJECTVERSION" -> currentVerStr,
        "LONGPROJECTVERSION" -> currentVerStr,
        "AUTHORS" -> mainAuthor,
        "MAINAUTHOR" -> mainAuthor,
        "SHORTPROJECTDESC" -> description.value,
        "EPUBPUBLISHER" -> mainAuthor)

      // must first add these modified settings to a new state by doing this:
      //  otherwise they won't be available for use (or set to what we just set them to)
      val newState = extracted.append(Seq(newTarget, newIncludeFilter, newPreproVars), readState)

      // run the preprocess task so that it will the settings we modified above
      val (newState2, result) = Project.extract(newState).runTask(preprocess in Preprocess, newState)
      newState2
    }
  }

}

val genSphinxConfigHelp = Help(
  Seq(
    "generate-sphinx-configFile" -> "Generates a Sphinx config.py file using the version info from version.sbt"
  ),
  Map(
    "generate-sphinx-configFile" ->
      """|Generates a Sphinx config.py file using the preprocess:preprocess task to replace template @VALUES@
        |1. Read the version info from version.sbt
        |2. Set preprocess:preprocessVars with the version info, authors, and project description
        |3. Run preprocess:preprocess using src/site-preprocess/[*.py & *.rst] as the source, and src/sphinx as the target
        |4. This should generate a config.py file that Sphinx will use when it generates documenation.
        |   Sphinx will read values from the config.py file and insert them. For example, the information shown for
        |   the copyright is read from the config.py file.""".stripMargin
  ))


val generateSphinxConfigCommand = Command.command("generate-sphinx-configFile", genSphinxConfigHelp)(generateSphinxConfigAction)

commands += generateSphinxConfigCommand

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
  //  runTest,
  // releaseStepInputTask(scripted, " com.typesafe.sbt.packager.universal/* debian/* rpm/* docker/* ash/* jar/* bash/* jdkpackager/*"),
  setReleaseVersion,
  commitReleaseVersion,
  tagRelease,
  //  publishArtifacts,
  setNextVersion,
  commitNextVersion,
  releaseStepCommand(generateSphinxConfigCommand),
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

  myPsks.foreach((currPsk: Def.Setting[_]) => {
    val desc = currPsk.key.key.description.getOrElse("")
    val currScope = currPsk.key.scope
    val currConfigKey: ConfigKey = currScope.config.toOption.get

    println(s" this key: ${currPsk.key.key} label: ${currPsk.key.key.label} desc: $desc")
    println(s"   Scope: config: ${currConfigKey.name}, task: ${currScope.task}, project: ${currScope.project}, extra key(s): ${currScope.extra} ")
    val displaySepString = ""
    println(s"    scope is displayed as ${Scope.display(currScope, displaySepString)}")
    println(s"  defined at this source position: ${currPsk.pos}")
    println("")
  })

}


