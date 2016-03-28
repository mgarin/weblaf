[![Build Status](https://travis-ci.org/Sciss/weblaf.svg?branch=sbtfied)](https://travis-ci.org/Sciss/weblaf)
[![Maven Central](https://maven-badges.herokuapp.com/maven-central/de.sciss/weblaf/badge.svg)](https://maven-badges.herokuapp.com/maven-central/de.sciss/weblaf)

# README - sbt build

This branch contains a build file for [sbt](http://www.scala-sbt.org/), a different build tool. This purpose of this build is 

- to publish artifacts to Maven Central
- to be able to publish at a faster pace than the original project

To accomplish this, and to avoid confusion with the original project, we use a different group-identifier and version.
The group-id is now `"de.sciss"` instead of `"com.alee"`, however the library is identical and the look-and-feel base 
class is still `com.alee.laf.WebLookAndFeel`. The `pom.xml` file contains the necessary dependencies which are also 
available from Maven Central and will be automatically retrieved when using this artifact in a Maven or sbt build.

## Published artifacts

WebLaF can be used in your maven project using the following information:

    <dependency>
      <groupId>de.sciss</groupId>
      <artifactId>weblaf</artifactId>
      <version>{v}</version>
    </dependency>

Or in an sbt based project:

    "de.sciss" % "weblaf" % v

The current version `v` is `"2.0.0"` (no relation to original WebLaF project version).

Note that the dependency on RSyntaxTextArea is not declared, so if you want to use `StyleEditor`,
you have to add the additional dependency on this library.

## Building

The base directory for the sbt build is `build-sbt`. It contains symbolic links to the sources of the root directory.

For simplicity, the `sbt` shell script by [Paul Phillips](https://github.com/paulp/sbt-extras) is included, 
made available under a BSD license. For example, you can publish a locally available artifact
using `./sbt publish-local`.

## Running

To run the main project's info dialog: `sbt weblaf-ui/run`. To run the demo application: `sbt weblaf-demo/run`.
