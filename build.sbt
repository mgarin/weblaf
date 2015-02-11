lazy val baseName = "WebLaF"

lazy val baseNameL = baseName.toLowerCase

// version       := "1.27.0"
lazy val isSnapshotVersion = true

// - generate debugging symbols
// - compile to 1.6 compatible class files
// - source adheres to Java 1.6 API
lazy val commonJavaOptions = Seq("-source", "1.6")

lazy val fullDescr = "WebLaf is a Java Swing Look and Feel and extended components library for cross-platform applications"

lazy val rSyntaxVersion = "2.5.6"

lazy val baseDir = file("build-sbt")

def mkVersion(base: File): String = {
  val propF = base / ".." / "build" / "version.properties"
  val prop  = new java.util.Properties()
  val r     = new java.io.FileReader(propF)
  prop.load(r)
  r.close()
  val major = prop.getProperty("version.number")
  val minor = prop.getProperty("build.number"  )
  s"$major.$minor${if (isSnapshotVersion) "-SNAPSHOT" else ""}"
}

lazy val commonSettings = Project.defaultSettings ++ Seq(
  // organization  := "com.alee"
  // we use this organization in order to publish to Sonatype Nexus (Maven Central)
  organization      := "de.sciss",
  scalaVersion      := "2.11.5",  // not used
  homepage          := Some(url("http://weblookandfeel.com")),
  licenses          := Seq("GPL v3+" -> url("http://www.gnu.org/licenses/gpl-3.0.txt")),
  crossPaths        := false,   // this is just a Java project
  autoScalaLibrary  := false,   // this is just a Java project
  javacOptions      := commonJavaOptions ++ Seq("-target", "1.6", "-g", "-Xlint:deprecation" /*, "-Xlint:unchecked" */),
  javacOptions in doc := commonJavaOptions,  // cf. sbt issue #355
  // this is used by LibraryInfoDialog.java, however assuming a jar file,
  // so it is not found when using `sbt run`.
  unmanagedResourceDirectories in Compile += baseDirectory.value / ".." / "licenses",
  // ---- publishing to Maven Central ----
  publishMavenStyle := true,
  publishTo := {
    Some(if (version.value endsWith "-SNAPSHOT")
      "Sonatype Snapshots" at "https://oss.sonatype.org/content/repositories/snapshots"
    else
      "Sonatype Releases"  at "https://oss.sonatype.org/service/local/staging/deploy/maven2"
    )
  },
  publishArtifact in Test := false,
  pomIncludeRepository := { _ => false },
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
)

// ---- projects ----

lazy val full = Project(
  id            = baseNameL,
  base          = baseDir,
  aggregate     = Seq(core, ui),  // exclude `demo` here
  dependencies  = Seq(core, ui),  // exclude `demo` here
  settings      = commonSettings ++ Seq(
    name := baseName,
    description := fullDescr,
    // version is determined from version.properties
    version := mkVersion(baseDirectory.value),
    publishArtifact in (Compile, packageBin) := false, // there are no binaries
    publishArtifact in (Compile, packageDoc) := false, // there are no javadocs
    publishArtifact in (Compile, packageSrc) := false  // there are no sources
  )
)

lazy val core: Project = Project(
  id        = s"$baseNameL-core",
  base      = baseDir / "core",
  settings  = commonSettings ++ Seq(
    name        := s"$baseName-core",
    description := "Core components for WebLaf",
    version     := mkVersion(baseDirectory.value / ".."),
    libraryDependencies ++= Seq(
      "com.thoughtworks.xstream" % "xstream"            % "1.4.7" exclude("xpp3", "xpp3_min") exclude("xmlpull", "xmlpull"),
      "net.htmlparser.jericho"   % "jericho-html"       % "3.3",
      "com.mortennobel"          % "java-image-scaling" % "0.8.5",
      "org.slf4j"                % "slf4j-api"          % "1.7.7",
      "org.slf4j"                % "slf4j-simple"       % "1.7.7"
    ),
    javaSource        in Compile := baseDirectory.value / ".." / ".." / "modules" / "core" / "src",
    resourceDirectory in Compile := baseDirectory.value / ".." / ".." / "modules" / "core" / "src",
    // excludeFilter in (Compile, unmanagedSources)   := new SimpleFileFilter(_.getPath.contains("/examples/")),
    excludeFilter in (Compile, unmanagedResources) := "*.java"
  )
)

lazy val ui = Project(
  id        = s"$baseNameL-ui",
  base      = baseDir / "ui",
  dependencies = Seq(core),
  settings  = commonSettings ++ Seq(
    name        := s"$baseName-ui",
    description := fullDescr,
    version     := mkVersion(baseDirectory.value / ".."),
    libraryDependencies ++= Seq(
      "com.fifesoft" % "rsyntaxtextarea" % rSyntaxVersion % "provided"  // we don't want to drag this under in 99% of cases
    ),
    mainClass in (Compile,run) := Some("com.alee.laf.LibraryInfoDialog"),
    javaSource        in Compile := baseDirectory.value / ".." / ".." / "modules" / "ui" / "src",
    resourceDirectory in Compile := baseDirectory.value / ".." / ".." / "modules" / "ui" / "src",
    // excludeFilter in (Compile, unmanagedSources)   := new SimpleFileFilter(_.getPath.contains("/examples/")),
    excludeFilter in (Compile, unmanagedResources) := "*.java"
  )
)

lazy val demo = Project(
  id        = s"$baseNameL-demo",
  base      = baseDir / "demo",
  dependencies = Seq(core, ui),
  settings  = commonSettings ++ Seq(
    name        := s"$baseName-demo",
    description := "Demo examples for WebLaf",
    version     := mkVersion(baseDirectory.value / ".."),
    libraryDependencies ++= Seq(
      "com.fifesoft" % "rsyntaxtextarea" % rSyntaxVersion
    ),
    mainClass in (Compile,run) := Some("com.alee.examples.WebLookAndFeelDemo"),
    javaSource        in Compile := baseDirectory.value / ".." / ".." / "modules" / "demo" / "src",
    resourceDirectory in Compile := baseDirectory.value / ".." / ".." / "modules" / "demo" / "src",
    // excludeFilter in (Compile, unmanagedSources)   := new SimpleFileFilter(_.getPath.contains("/examples/")),
    excludeFilter in (Compile, unmanagedResources) := "*.java"
  )
)
