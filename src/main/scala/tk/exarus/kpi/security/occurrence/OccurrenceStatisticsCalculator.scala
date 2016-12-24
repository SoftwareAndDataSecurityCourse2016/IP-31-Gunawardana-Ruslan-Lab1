package tk.exarus.kpi.security.occurrence

import tk.exarus.kpi.security.StatisticsCalculator

import scala.collection.Map

/**
  * Implementation of `StatisticsCalculator`.
  *
  * @author Ruslan Gunawardana
  */
class OccurrenceStatisticsCalculator extends StatisticsCalculator {
  override protected def occurrencesToFrequency(nGramToOccurrence: Map[String, Int],
                                                key: String, occurrences: Int): Double = {
    occurrences.toDouble
  }
}
