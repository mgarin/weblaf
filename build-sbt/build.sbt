lazy val baseName           = "WebLaF"
lazy val baseNameL          = baseName.toLowerCase
lazy val fullDescr          = "WebLaf is a Java Swing Look and Feel and extended components library for cross-platform applications"

lazy val useOurOwnVersion   = true      // detaches artifact from original WebLaF numbering
lazy val ownVersion         = "2.1.0"   // we deliberately make a jump here to avoid confusion with original version
lazy val upstreamIsSnapshot = true      // only used when `useOurOwnVersion` is `false`!

// - generate debugging symbols
// - compile to 1.6 compatible class files
// - source adheres to Java 1.6 API
lazy val commonJavaOptions  = Seq("-source", "1.6")

// ---- core dependencies ----
lazy val imageScalingVersion= "0.8.6"
lazy val xstreamVersion     = "1.4.8"
lazy val jerichoVersion     = "3.3" // note: "3.4" is not Java 6 compatible
lazy val slfVersion         = "1.7.18"

// ---- ui dependencies ----
lazy val rSyntaxVersion     = "2.5.8"

// ---- demo dependencies ----
lazy val salamanderVersion  = "1.0"

def mkVersion(base: File): String =
  if (useOurOwnVersion)
    ownVersion
  else {
    val propF = base / ".." / "build" / "version.properties"
    val prop  = new java.util.Properties()
    val r     = new java.io.FileReader(propF)
    prop.load(r)
    r.close()
    val major = prop.getProperty("version.number")
    val minor = prop.getProperty("build.number"  )
    s"$major.$minor${if (upstreamIsSnapshot) "-SNAPSHOT" else ""}"
  }

lazy val commonSettings = Seq(
  // organization  := "com.alee"
  // we use this organization in order to publish to Sonatype Nexus (Maven Central)
  organization      := "de.sciss",
  scalaVersion      := "2.11.8",  // not used
  homepage          := Some(url("http://weblookandfeel.com")),
  licenses          := Seq("GPL v3+" -> url("http://www.gnu.org/licenses/gpl-3.0.txt")),
  crossPaths        := false,   // this is just a Java project
  autoScalaLibrary  := false,   // this is just a Java project
  javacOptions      := commonJavaOptions ++ Seq("-target", "1.6", "-g", "-Xlint:deprecation" /*, "-Xlint:unchecked" */),
  javacOptions in doc := commonJavaOptions ++ Seq("-Xdoclint:all,-reference"),  // cf. sbt issue #355
  // this is used by LibraryInfoDialog.java, however assuming a jar file,
  // so it is not found when using `sbt run`.
//  unmanagedResourceDirectories in Compile += baseDirectory.value / ".." / "licenses",
  // ---- publishing to Maven Central ----
  publishMavenStyle := true,
  publishTo := {
    Some(if (isSnapshot.value)
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
      <developer>
        <id>sciss</id>
        <name>Hans Holger Rutz</name>
        <url>http://www.sciss.de</url>
      </developer>
    </developers>
  }
)

// ---- projects ----

lazy val full = Project(
  id            = baseNameL,
  base          = file("."),
  aggregate     = Seq(core, ui),  // exclude `demo` here
  dependencies  = Seq(core, ui),  // exclude `demo` here
  settings      = commonSettings ++ Seq(
    name := baseName,
    description := fullDescr,
    // version is determined from version.properties
    version := mkVersion(baseDirectory.value),
    publishArtifact in (Compile, packageBin) := false, // there are no binaries
    publishArtifact in (Compile, packageDoc) := false, // there are no java-docs
    publishArtifact in (Compile, packageSrc) := false  // there are no sources
  )
)

lazy val core = Project(id = s"$baseNameL-core", base = file("core"))
  .settings(commonSettings)
  .settings(
    name        := s"$baseName-core",
    description := "Core components for WebLaf",
    version     := mkVersion(baseDirectory.value / ".."),
    libraryDependencies ++= Seq(
      "com.thoughtworks.xstream" % "xstream"            % xstreamVersion exclude("xpp3", "xpp3_min") exclude("xmlpull", "xmlpull"),
      "net.htmlparser.jericho"   % "jericho-html"       % jerichoVersion,
      "com.mortennobel"          % "java-image-scaling" % imageScalingVersion,
      "org.slf4j"                % "slf4j-api"          % slfVersion,
      "org.slf4j"                % "slf4j-simple"       % slfVersion
    ),
    // javaSource        in Compile := baseDirectory.value / ".." / ".." / "modules" / "core" / "src",
    // resourceDirectory in Compile := baseDirectory.value / ".." / ".." / "modules" / "core" / "src",
    // excludeFilter in (Compile, unmanagedSources)   := new SimpleFileFilter(_.getPath.contains("/examples/")),
    excludeFilter in (Compile, unmanagedResources) := "*.java"
  )

lazy val ui = Project(id = s"$baseNameL-ui", base = file("ui"))
  .dependsOn(core)
  .settings(commonSettings)
  .settings(
    name        := s"$baseName-ui",
    description := fullDescr,
    version     := mkVersion(baseDirectory.value / ".."),
    libraryDependencies ++= Seq(
      "com.fifesoft" % "rsyntaxtextarea" % rSyntaxVersion % "provided"  // we don't want to drag this under in 99% of cases
    ),
    mainClass in (Compile,run) := Some("com.alee.laf.LibraryInfoDialog"),
    // javaSource        in Compile := baseDirectory.value / ".." / ".." / "modules" / "ui" / "src",
    // resourceDirectory in Compile := baseDirectory.value / ".." / ".." / "modules" / "ui" / "src",
    // excludeFilter in (Compile, unmanagedSources)   := new SimpleFileFilter(_.getPath.contains("/examples/")),
    excludeFilter in (Compile, unmanagedResources) := "*.java"
  )

lazy val demo = Project(id = s"$baseNameL-demo", base = file("demo"))
  .dependsOn(core, ui)
  .settings(commonSettings)
  .settings(
    name        := s"$baseName-demo",
    description := "Demo examples for WebLaf",
    version     := mkVersion(baseDirectory.value / ".."),
    libraryDependencies ++= Seq(
      "com.fifesoft"   % "rsyntaxtextarea" % rSyntaxVersion,
      "com.kitfox.svg" % "svg-salamander"  % salamanderVersion
    ),
    fork in run := true,
    mainClass in (Compile,run) := Some("com.alee.examples.WebLookAndFeelDemo"),
    // javaSource        in Compile := baseDirectory.value / ".." / ".." / "modules" / "demo" / "src",
    // resourceDirectory in Compile := baseDirectory.value / ".." / ".." / "modules" / "demo" / "src",
    // excludeFilter in (Compile, unmanagedSources)   := new SimpleFileFilter(_.getPath.contains("/examples/")),
    excludeFilter in (Compile, unmanagedResources) := "*.java"
  )
