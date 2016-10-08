package ua.kpi.ip31.gunawardana.repository

/**
  * Grants access to some language statistics.
  * Each language should supply it's own statistics.
  *
  * @author Ruslan Gunawardana
  */
trait TextStatisticsRepository {
  def firstOrderStatistics: Map[String, Double]

  def secondOrderStatistics: Map[String, Double]

  def thirdOrderStatistics: Map[String, Double]

  def alphabet: String

  def alphabetSortedByStats(stats: Map[String, Double]): String
}
