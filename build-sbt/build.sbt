name             := "WebLaF"

// organization  := "com.alee"

// we use this organization in order to publish to Sonatype Nexus (Maven Central)
organization     := "de.sciss"

// version       := "1.27.0"

// version is determined from version.properties
version := {
  val propF = baseDirectory.value / ".." / "build" / "version.properties"
  val prop  = new java.util.Properties()
  val r     = new java.io.FileReader(propF)
  prop.load(r)
  r.close()
  val major = prop.getProperty("version.number")
  val minor = prop.getProperty("build.number"  )
  s"$major.$minor"
}

homepage         := Some(url("http://weblookandfeel.com"))

licenses         := Seq("GPL v3+" -> url("http://www.gnu.org/licenses/gpl-3.0.txt"))

description      := "WebLaf is a Java Swing Look and Feel and extended components library for cross-platform applications"

// For pure Java projects, do not use
// Scala version based artifacts
crossPaths       := false

// For pure Java projects, sbt needs to be told not to
// add the Scala runtime library to the dependencies
autoScalaLibrary := false

// - generate debugging symbols
// - compile to 1.6 compatible class files
// - source adheres to Java 1.6 API
lazy val commonJavaOptions = Seq("-source", "1.6")

javacOptions        := commonJavaOptions ++ Seq("-target", "1.6", "-g", "-Xlint:deprecation" /*, "-Xlint:unchecked" */)

javacOptions in doc := commonJavaOptions   // cf. sbt issue #355

mainClass in (Compile,run) := Some("com.alee.laf.LibraryInfoDialog")

// sbt will always retrieve a Scala version,
// even if not used in the projects
scalaVersion     := "2.10.4"

libraryDependencies ++= Seq(
  "com.thoughtworks.xstream" % "xstream"            % "1.4.7" exclude("xpp3", "xpp3_min") exclude("xmlpull", "xmlpull"),
  "net.htmlparser.jericho"   % "jericho-html"       % "3.3",
  "com.fifesoft"             % "rsyntaxtextarea"    % "2.5.0",
  "com.mortennobel"          % "java-image-scaling" % "0.8.5"
)

retrieveManaged := true

// ---- paths ----

javaSource        in Compile := baseDirectory.value / ".." / "src"

resourceDirectory in Compile := baseDirectory.value / ".." / "src"

excludeFilter in (Compile, unmanagedSources)   := new SimpleFileFilter(_.getPath.contains("/examples/"))

excludeFilter in (Compile, unmanagedResources) := "*.java"

// this is used by LibraryInfoDialog.java, however assuming a jar file,
// so it is not found when using `sbt run`.
unmanagedResourceDirectories in Compile += baseDirectory.value / ".." / "licenses"

// ---- publishing to Maven Central ----

publishMavenStyle in ThisBuild := true

publishTo in ThisBuild :=
  Some(if (version.value endsWith "-SNAPSHOT")
    "Sonatype Snapshots" at "https://oss.sonatype.org/content/repositories/snapshots"
  else
    "Sonatype Releases"  at "https://oss.sonatype.org/service/local/staging/deploy/maven2"
  )

publishArtifact in Test in ThisBuild := false

pomIncludeRepository in ThisBuild := { _ => false }

pomExtra := { val n = name.value.toLowerCase
  <scm>
    <url>git@github.com:mgarin/{n}.git</url>
    <connection>scm:git:git@github.com:mgarin/{n}.git</connection>
  </scm>
  <developers>
    <developer>
      <id>mgarin</id>
      <name>Mikle Garin</name>
      <url>http://weblookandfeel.com</url>
    </developer>
  </developers>
}

