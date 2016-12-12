package ua.kpi.ip31.gunawardana.repository

/**
  * Grants access to English statistics.
  *
  * @author Ruslan Gunawardana
  */
class EnglishTextStatisticsRepository extends TextStatisticsRepository {

  override val alphabet = "abcdefghijklmnopqrstuvwxyz"

  override def firstOrderStatistics: Map[Char, Double] = loadStatisticsFrom(1) map { case (k, v) => k(0) -> v }

  override def secondOrderStatistics: Map[String, Double] = loadStatisticsFrom(2)

  override def thirdOrderStatistics: Map[String, Double] = loadStatisticsFrom(3)

  import EnglishTextStatisticsRepository._

  private def loadStatisticsFrom(order: Int): Map[String, Double] = {
    val path = fileByOrder(order)
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
}

private object EnglishTextStatisticsRepository {
  private val fileByOrder = Map(
    1 -> "/first-order-statistics.csv",
    2 -> "/second-order-statistics.csv",
    3 -> "/third-order-statistics.csv")

  private def parseCsvLine(line: String): Array[String] = line.split(",").map(_.trim)
}
