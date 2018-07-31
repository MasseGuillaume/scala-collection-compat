import org.jsoup.Jsoup

import com.github.nscala_time.time.Imports._

import java.nio.file._
import java.net.URL

object LatestScala {

  private val url = "https://scala-ci.typesafe.com/artifactory/scala-integration/org/scala-lang/scala-library/"
  private val index = new URL(url).openStream()
  private val html = scala.io.Source.fromInputStream(index).mkString
  index.close

  private val doc = Jsoup.parse(html, url)
  private val pre = doc.select("pre").get(1).text
  private val versionsAndDateRaw = pre.split("\n").drop(1).dropRight(1)
  private val dateFormat = DateTimeFormat.forPattern("dd-MMM-yyyy HH:mm")
  private val versionsAndDate =
    versionsAndDateRaw.map{line =>
      val Array(version, dateRaw) = line.split("/")
      val dateClean = dateRaw.dropRight(1).trim
      val date = DateTime.parse(dateClean, dateFormat)
      (version, date)
    }

  private def Descending[T : Ordering] = implicitly[Ordering[T]].reverse

  private val (version, date) = versionsAndDate.sortBy(_._2)(Descending).head
  val latestVersion = version
  val lastestDate = dateFormat.print(date) 
}