package ua.kpi.ip31.gunawardana

/**
  * Thread-safe. Immutable.
  *
  * @author Ruslan Gunawardana
  */
class StatisticsCalculator {
  def calculateThirdOrderStatisticsFor(text: String): Map[String, Double] = {
    val occurrenceMap = new collection.mutable.HashMap[String, Double]
    text sliding 3 foreach { windowedText =>
      occurrenceMap(windowedText) = occurrenceMap.getOrElse(windowedText, 0.0) + 1
    }
    (occurrenceMap mapValues (_ / text.length)).toMap
  }

  def chiSquareTest(expectedValues: Map[String, Double], actualValues: Map[String, Double]): Double = {
    (expectedValues map { case (key, expected) =>
      val actual = actualValues.getOrElse(key, 0.0)
      math.pow(actual - expected, 2) / expected
    }).sum
  }
}
