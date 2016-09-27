package ua.kpi.ip31.gunawardana.repository

/**
  * Thread-safety not verified.
  * Immutability not verified.
  *
  * @author Ruslan Gunawardana
  */
trait TextStatisticsRepository {
  def thirdOrderStatistics: Map[String, Double]
  def alphabet: String
}
