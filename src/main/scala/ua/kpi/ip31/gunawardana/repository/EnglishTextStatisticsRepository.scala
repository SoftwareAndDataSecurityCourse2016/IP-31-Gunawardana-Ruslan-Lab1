package ua.kpi.ip31.gunawardana.repository

/**
  * @author Ruslan Gunawardana
  */
class EnglishTextStatisticsRepository extends TextStatisticsRepository {

  import EnglishTextStatisticsRepository._

  override def thirdOrderStatistics: Map[String, Double] = {
    val fileSource = io.Source.fromInputStream(getClass.getResourceAsStream("/third-order-statistics.csv"))
    val rows = (fileSource.getLines map parseCsvLine).toIterable
    val columnTitles = rows.head.tail map (_ charAt 0)
    val statistics = rows
      .tail
      .flatMap { row =>
        val rowElements = row.tail map (_.toDouble)
        val tuples: Array[(Double, Char)] = rowElements zip columnTitles
        (rowElements, columnTitles).zipped map { (e, columnTitle) => row.head + columnTitle -> e }
      }
      .filter { e => e._2 > 0 }
      .toMap
    fileSource.close
    statistics
  }

  override def alphabet: String = {
    "abcdefghijklmnopqrstuvwxyz"
  }
}

object EnglishTextStatisticsRepository {
  private def parseCsvLine(line: String): Array[String] = line.split(",").map(_.trim)
}
