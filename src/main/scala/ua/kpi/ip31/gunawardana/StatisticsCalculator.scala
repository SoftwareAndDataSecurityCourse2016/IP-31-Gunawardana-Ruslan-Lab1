package ua.kpi.ip31.gunawardana

import scala.collection.mutable

/**
  * Thread-safe. Immutable.
  *
  * @author Ruslan Gunawardana
  */
class StatisticsCalculator {
  def firstOrderStatistics(text: String): Map[Char, Double] = {
    val occurrenceMap = new mutable.HashMap[Char, Double]
    text foreach { c =>
      occurrenceMap(c) = occurrenceMap.getOrElse(c, 0.0) + 1
    }
    occurrenceMap.toMap
  }

  def orderStatistics(text: String, order: Int): Map[String, Double] = {
    if (order == 1) {
      firstOrderStatistics(text) map { case (k, v) => k.toString -> v }
    } else {
      val occurrenceMap = new mutable.HashMap[String, Double]
      text sliding order foreach { windowedText =>
        occurrenceMap(windowedText) = occurrenceMap.getOrElse(windowedText, 0.0) + 1
      }
      val relativeFrequencyMap = occurrenceMap map { case (k, v) =>
        val firstLetterCount = text count (_ == k(0))
        k -> v / firstLetterCount
      }
      relativeFrequencyMap.toMap
    }
  }

  def chiSquareTest(expectedValues: Map[String, Double], actualValues: Map[String, Double]): Double = {
    (expectedValues.par map { case (key, expected) =>
      val actual = actualValues.getOrElse(key, 0.0)
      math.pow(actual - expected, 2) / expected
    }).sum
  }
}
