package tk.exarus.kpi.security.occurrence

import tk.exarus.kpi.security.TextStatisticsRepository

import scala.io.Source.fromResource

/**
  * @author Ruslan Gunawardana
  */
class OccurrenceStatisticsRepository extends TextStatisticsRepository {

  import OccurrenceStatisticsRepository.{englishAlphabet, pathByOrder}

  override def alphabet: String = englishAlphabet

  override def firstOrderStatistics: Map[Char, Double] = loadStatisticsFor(1) map { case (k, v) => k(0) -> v }

  override def secondOrderStatistics: Map[String, Double] = loadStatisticsFor(2)

  override def thirdOrderStatistics: Map[String, Double] = loadStatisticsFor(3)

  private def loadStatisticsFor(order: Int): Map[String, Double] = {
    val source = fromResource(pathByOrder(order))
    val stats = source.getLines
      .map(parseLine)
      .filter { case (k, v) => (k forall (alphabet contains _)) && v > 0 }
      .toMap
    source.close()
    stats
  }

  private def parseLine(line: String): (String, Double) = {
    val tokens = line split " " map (_.trim)
    tokens(0).toLowerCase -> tokens(1).toDouble
  }
}

object OccurrenceStatisticsRepository {
  private val englishAlphabet = "abcdefghijklmnopqrstuvwxyz"

  private val pathByOrder = {
    val statisticsPathRoot = "ngramm-occurrences/"
    val fileByOrder = Map(
      1 -> "1.txt",
      2 -> "2.txt",
      3 -> "3.txt")
    fileByOrder mapValues (statisticsPathRoot + _)
  }
}