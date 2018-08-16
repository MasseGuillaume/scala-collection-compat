import sbt._
import sbt.Keys._

import sbtcrossproject.{Platform, CrossProject}
import sbtcrossproject.CrossPlugin.autoImport.{crossProject, CrossType}
import sbtcrossproject.CrossPlugin.autoImport._
import scalajscrossproject.ScalaJSCrossPlugin.autoImport._
import org.scalajs.sbtplugin.ScalaJSJUnitPlugin


import java.io.File

/** MultiScalaCrossProject and MultiScalaProject are an alternative to crossScalaVersion
 * it allows you to create a template for a sbt project you can instanciate with a
 * specific scala version.
 *
 * {{{
 * // create a project template
 * val myProject = MultiScalaProject(
 *   "name",
 *   "path/to/dir",
 *   _.settings(...) // Project => Project (scala version independent configurations)
 * )
 *
 * // instanciate a sbt project
 * lazy val myProject211 = myProject("2.11.12", _.settings(...) /* scala version dependent configurations */)
 * lazy val myProject212 = myProject("2.12.6" , _.settings(...))
 * // ...
 * }}}
 */
trait MultiScala {
  def majorMinor(in: String): String = {
    val Array(major, minor, _) = in.split("\\.")
    major + minor
  }

  def projectIdPerScala(name: String, scalaV: String): String = s"$name${majorMinor(scalaV)}"

  def srcFull(base: String): Seq[Def.Setting[_]] = {
    Seq(
      unmanagedSourceDirectories in Compile +=
        (baseDirectory in ThisBuild).value / base / "src" / "main" / "scala",
      unmanagedSourceDirectories in Compile +=
        (baseDirectory in ThisBuild).value / base / "src" / "main" / ("scala-" + scalaBinaryVersion.value),
      unmanagedSourceDirectories in Compile ++= {
        val sourceDir = (baseDirectory in ThisBuild).value / base / "src" / "main"
        CrossVersion.partialVersion(scalaVersion.value) match {
          case Some((2, n)) if n >= 12 => List(sourceDir / "scala-2.12+")
          case _                       => Nil
        }
      },
      unmanagedSourceDirectories in Test +=
        (baseDirectory in ThisBuild).value / base / "src" / "test" / "scala",
      unmanagedSourceDirectories in Test +=
        (baseDirectory in ThisBuild).value / base / "src" / "test" / ("scala-" + scalaBinaryVersion.value),
      unmanagedSourceDirectories in Test ++= {
        val sourceDir = (baseDirectory in ThisBuild).value / base / "src" / "test"
        CrossVersion.partialVersion(scalaVersion.value) match {
          case Some((2, n)) if n >= 12 => List(sourceDir / "scala-2.12+")
          case _                       => Nil
        }
      }
    )
  }
}

object MultiScalaCrossProject {
  def apply(platforms: Platform*)(
    name: String,
    base: String,
    configure: CrossProject => CrossProject): MultiScalaCrossProject =
      new MultiScalaCrossProject(platforms, name, base, configure)
}

class MultiScalaCrossProject(platforms: Seq[Platform],
                             name: String,
                             base: String,
                             configure: CrossProject => CrossProject)
    extends MultiScala {

  def srcMain: String = s"$base/src/main"

  def apply(scalaV: String): CrossProject = apply(scalaV, scalaV, x => x)

  def apply(scalaV: String, configurePerScala: CrossProject => CrossProject): CrossProject =
    apply(scalaV, scalaV, configurePerScala)


  def apply(
      scalaV: String,
      scalaVJs: String,
      configurePerScala: CrossProject => CrossProject = x => x
  ): CrossProject = {
    val projectId = projectIdPerScala(name, scalaV)
    val resultingProject =
      CrossProject(
        id = projectId,
        base = file(s".cross/$projectId")
      )(platforms: _*)
        .crossType(CrossType.Full)
        .withoutSuffixFor(JVMPlatform)
        .settings(moduleName := name)
        .jvmSettings(
          scalaVersion := scalaV,
          Shared.junit
        )
        .jsSettings(scalaVersion := scalaVJs)
        .jsConfigure(_.enablePlugins(ScalaJSJUnitPlugin))
        .settings(srcFull(base))

    configurePerScala(
      configure(resultingProject)
        .jsSettings(fork in Test := false) // Scala.js cannot run forked tests
    )
  }
}

object MultiScalaProject {
  def apply(name: String, configure: Project => Project): MultiScalaProject =
    new MultiScalaProject(name, s"scalafix-$name", configure)

  def apply(name: String, base: String, configure: Project => Project): MultiScalaProject =
    new MultiScalaProject(name, base, configure)
}

class MultiScalaProject(name: String, base: String, configure: Project => Project)
    extends MultiScala {

  def srcMain: String = s"$base/src/main"

  def apply(scalaV: String, configurePerScala: Project => Project = x => x): Project = {
    val fullName  = s"scalafix-$name"
    val projectId = projectIdPerScala(name, scalaV)
    val resultingProject =
      Project(id = projectId, base = file(s".cross/$projectId"))
        .settings(
          moduleName := fullName,
          scalaVersion := scalaV
        )
        .settings(srcFull(base))

    configurePerScala(configure(resultingProject))
  }
}
