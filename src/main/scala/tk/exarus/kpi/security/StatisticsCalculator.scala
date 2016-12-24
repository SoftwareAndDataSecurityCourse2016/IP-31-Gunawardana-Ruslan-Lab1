package tk.exarus.kpi.security

import scala.collection.mutable

/**
  * Created by exarus on 12/20/2016.
  */
trait StatisticsCalculator {
  def orderStatistics(text: String, order: Int): Map[String, Double] = {
    if (order < 1) {
      throw new IllegalArgumentException("Order should be a positive integer")
    } else if (order == 1) {
      firstOrderStatistics(text) map { case (k, v) => k.toString -> v }
    } else {
      val nGramToOccurrence = mutable.Map[String, Int]()
      text sliding order foreach { textWindow =>
        nGramToOccurrence(textWindow) = nGramToOccurrence.getOrElse(textWindow, 0) + 1
      }
      nGramToOccurrence
        .map { case (k, v) => k -> occurrencesToFrequency(nGramToOccurrence, k, v) }
        .toMap
    }
  }

  def firstOrderStatistics(text: String): Map[Char, Double] = {
    val occurrenceMap = mutable.Map[Char, Double]()
    text foreach { c =>
      occurrenceMap(c) = occurrenceMap.getOrElse(c, 0.0) + 1
    }
    occurrenceMap
      .transform { (_, v) => v / text.length }
      .toMap
  }

  protected def occurrencesToFrequency(nGramToOccurrence: collection.Map[String, Int],
                                       key: String, occurrences: Int): Double
}
