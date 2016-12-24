package tk.exarus.kpi.security.combination

import tk.exarus.kpi.security.StatisticsCalculator

import scala.collection.{Map, mutable}

/**
  * Thread-safe. Immutable.
  *
  * @author Ruslan Gunawardana
  */
class CombinationStatisticsCalculator extends StatisticsCalculator {
  override protected def occurrencesToFrequency(nGramToOccurrence: Map[String, Int],
                                                key: String, occurrences: Int): Double = {
    val sameInitNGramCount = nGramToOccurrence.keys count (_.init == key.init)
    occurrences / sameInitNGramCount
  }
}
