# README - sbt build

This directory contains a build file for [sbt](http://www.scala-sbt.org/), a different build tool. This purpose of this build is to publish the simple artifact to Maven Central. This currently uses the group-ID `"de.sciss"` instead of `"com.alee"`, however the library is identical and the look-and-feel base class is still `com.alee.laf.WebLookAndFeel`. The `pom.xml` file contains the necessary dependencies which are also available from Maven Central and will be automatically retrieved when using this artifact in a Maven or sbt build.

For simplicity, the `sbt` shell script by [Paul Phillips](https://github.com/paulp/sbt-extras) is included, made available under a BSD license. For example, you can publish a locally available artifact using `./sbt publish-local`.

## Published artifacts

WebLaF can be used in your maven project using the following information:

    <dependency>
      <groupId>de.sciss</groupId>
      <artifactId>weblaf</artifactId>
      <version>1.28</version>
    </dependency>

Or in an sbt based project:

    "de.sciss" % "weblaf" % "1.28"

Note that the dependency on RSyntaxTextArea is not declared, so if you want to use `StyleEditor`, you have to add the additional dependency on this library.