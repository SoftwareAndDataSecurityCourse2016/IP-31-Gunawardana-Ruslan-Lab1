package tk.exarus.kpi.security

/**
  * Created by exarus on 12/20/2016.
  */
trait StatisticsCalculator {
  def orderStatistics(text: String, order: Int): Map[String, Double]

  def firstOrderStatistics(text: String): Map[Char, Double]
}
