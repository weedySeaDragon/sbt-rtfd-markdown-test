name := "sbt-rtfd-markdown-test"
organization := "com.ashleycaroline"
val author = "ashley engelund (weedy-sea-dragon @ github)"

version := "1.0"
scalaVersion := "2.11.8"


// configure github page
enablePlugins(SphinxPlugin, SiteScaladocPlugin, PreprocessPlugin)

// Preprocess settings for generating documentation:


//preprocessVars in Preprocess := Map("VERSION" -> latestVersion)

// Preprocess defaults: includeFilter includes *rst, mapping for vars has VERSION.  So we don't need to set those
sourceDirectory in Preprocess := sourceDirectory.value / "site-preprocess" / "sphinx"
target in Preprocess := sourceDirectory.value / "sphinx"
