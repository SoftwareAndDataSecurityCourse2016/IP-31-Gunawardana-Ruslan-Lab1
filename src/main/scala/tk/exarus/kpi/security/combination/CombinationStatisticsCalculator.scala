package tk.exarus.kpi.security.combination

import scala.collection.mutable

/**
  * Thread-safe. Immutable.
  *
  * @author Ruslan Gunawardana
  */
class CombinationStatisticsCalculator {
  def orderStatistics(text: String, order: Int): Map[String, Double] = {
    if (order < 1) {
      throw new IllegalArgumentException("Order should be a positive integer")
    } else if (order == 1) {
      firstOrderStatistics(text) map { case (k, v) => k.toString -> v }
    } else {
      val nGramFrequency = mutable.Map[String, Double]()
      text sliding order foreach { textWindow =>
        nGramFrequency(textWindow) = nGramFrequency.getOrElse(textWindow, 0.0) + 1
      }
      nGramFrequency transform { (k, v) =>
        val sameInitNGramCount = nGramFrequency.keys count (_.init == k.init)
        v / sameInitNGramCount
      }
      nGramFrequency.toMap
    }
  }

  def firstOrderStatistics(text: String): Map[Char, Double] = {
    val occurrenceMap = mutable.Map[Char, Double]()
    text foreach { c =>
      occurrenceMap(c) = occurrenceMap.getOrElse(c, 0.0) + 1
    }
    occurrenceMap transform ((_, v) => v / text.length)
    occurrenceMap.toMap
  }
}
