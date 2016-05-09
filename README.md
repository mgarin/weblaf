[![Build Status](https://travis-ci.org/Sciss/weblaf.svg?branch=sbtfied)](https://travis-ci.org/Sciss/weblaf)
[![Maven Central](https://maven-badges.herokuapp.com/maven-central/de.sciss/weblaf/badge.svg)](https://maven-badges.herokuapp.com/maven-central/de.sciss/weblaf)

# Web Look-and-Feel

This is a fork of the [Web Look-and-Feel project](https://github.com/mgarin/weblaf), a cross platform look-and-feel
for JVM desktop applications using the Swing toolkit. Please also see the [original README](README-ORIG.md).

This fork is mostly identical with upstream, but includes a few fixes required for interop with 
the [Submin](https://github.com/Sciss/Submin) derivate that has a light and dark skin.

This fork is published under the GNU General Public License v3+.

Furthermore, the purpose of this build is:

- to publish artifacts to Maven Central
- to be able to publish at a faster pace than the original project

To accomplish this, and to avoid confusion with the original project, we use a different group-identifier and version.
The group-id is now `"de.sciss"` instead of `"com.alee"`, however the library is identical and the look-and-feel base 
class is still `com.alee.laf.WebLookAndFeel`. The `pom.xml` file contains the necessary dependencies which are also 
available from Maven Central and will be automatically retrieved when using this artifact in a Maven or sbt build.

This branch is not built with ant but using [sbt](http://www.scala-sbt.org/), a modern build tool known from the Scala world.

## Published artifacts

WebLaF can be used in your maven project using the following information:

    <dependency>
      <groupId>de.sciss</groupId>
      <artifactId>weblaf</artifactId>
      <version>{v}</version>
    </dependency>

Or in an sbt based project:

    "de.sciss" % "weblaf" % v

The current version `v` is `"2.1.1"` (no relation to original WebLaF project version).

Note that the dependency on RSyntaxTextArea is not declared, so if you want to use `StyleEditor`,
you have to add the additional dependency on this library.

## Building

The base directory for the sbt build is `build-sbt`. It contains symbolic links to the sources of the root directory.

For simplicity, the `sbt` shell script by [Paul Phillips](https://github.com/paulp/sbt-extras) is included, 
made available under a BSD license. For example, you can publish a locally available artifact
using `./sbt publish-local`.

## Running

To run the main project's info dialog: `sbt weblaf-ui/run`. To run the demo application: `sbt weblaf-demo/run`.
