package tk.exarus.kpi.security

/**
  * Grants access to some language statistics.
  * Each language should supply it's own statistics.
  *
  * @author Ruslan Gunawardana
  */
trait TextStatisticsRepository {
  def alphabet: String

  def firstOrderStatistics: Map[Char, Double]

  def secondOrderStatistics: Map[String, Double]

  def thirdOrderStatistics: Map[String, Double]
}
