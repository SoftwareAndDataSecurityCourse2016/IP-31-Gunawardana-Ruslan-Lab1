package ua.kpi.ip31.gunawardana.repository

/**
  * Grants access to English statistics.
  *
  * @author Ruslan Gunawardana
  */
class EnglishTextStatisticsRepository extends TextStatisticsRepository {

  import EnglishTextStatisticsRepository._

  override def firstOrderStatistics: Map[Char, Double] =
    loadStatisticsFrom("/first-order-statistics.csv") map { case (k, v) => k(0) -> v }

  override def secondOrderStatistics: Map[String, Double] = loadStatisticsFrom("/second-order-statistics.csv")

  override def thirdOrderStatistics: Map[String, Double] = loadStatisticsFrom("/third-order-statistics.csv")

  private def loadStatisticsFrom(path: String): Map[String, Double] = {
    val source = io.Source.fromInputStream(getClass getResourceAsStream path)
    val rows = (source.getLines map parseCsvLine).toIterable
    val columnTitles = rows.head.tail map (_ charAt 0)
    val statistics = rows
      .tail
      .flatMap { row =>
        val rowElements = row.tail map (_.toDouble)
        rowElements zip columnTitles map { case (e, columnTitle) => row.head + columnTitle -> e }
      }
      .filter { case (k, v) => (k forall (alphabet contains _)) && v > 0 }
      .toMap
    source.close()
    statistics
  }

  override val alphabet = "abcdefghijklmnopqrstuvwxyz"

  override def alphabetSortedByStats(stats: Map[Char, Double]): String = {
    (alphabet
      map { c => c -> stats.getOrElse(c, 0.0) }
      sortBy (_._2)
      map (_._1)).mkString
  }
}

object EnglishTextStatisticsRepository {
  private def parseCsvLine(line: String): Array[String] = line.split(",").map(_.trim)
}
