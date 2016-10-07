package ua.kpi.ip31.gunawardana

import ua.kpi.ip31.gunawardana.repository.TextStatisticsRepository
import ua.kpi.ip31.gunawardana.util.StringPermutationsIterator

import scala.collection.{Iterator}

/**
  * Thread-safe. Immutable.
  *
  * @author Ruslan Gunawardana
  */
class FrequencyDecoder(statsRepo: TextStatisticsRepository, statsCalc: StatisticsCalculator) extends Decoder {
  override def decode(ciphertext: String): String = {
    val lowerCipherKey = bestCipherKeyFor(ciphertext.toLowerCase)
    val upperCipherKey = lowerCipherKey map { case (k, v) => k.toUpper -> v.toUpper }
    val cipherKey = (lowerCipherKey ++ upperCipherKey).toMap
    ciphertext map cipherKey
  }

  private def bestCipherKeyFor(ciphertext: String): Seq[(Char, Char)] = {
    val expectedFrequencyMap = statsRepo.thirdOrderStatistics
    val ciphertextFrequencyMap = statsCalc(ciphertext, 3)
    val alphabetSortedByExpectedStats = alphabetByStats(statsRepo.firstOrderStatistics)
    val alphabetSortedByCiphertextStats = alphabetByStats(statsCalc(ciphertext, 1))

    val alphabetChiSquares = alphabetSortedByCiphertextStats.distanceOnePermutations map { substitutionAlphabet =>
      val decipheringKey = (substitutionAlphabet zip alphabetSortedByExpectedStats).toMap
      val actualFrequencyMap = ciphertextFrequencyMap map { case (k, v) => (k map decipheringKey) -> v }
      val chiSquare = statsCalc.chiSquareTest(expectedFrequencyMap, actualFrequencyMap)
      substitutionAlphabet -> chiSquare
    }
    val sortedSubstitutionAlphabet = (alphabetChiSquares minBy (_._2))._1
    sortedSubstitutionAlphabet zip alphabetSortedByExpectedStats
  }

  private def alphabetByStats(stats: Map[String, Double]) = {
    (statsRepo.alphabet
      map { c => c -> stats.getOrElse(c.toString, 0.0) }
      sortBy (_._2)
      map (_._1)).mkString
  }

  implicit class StringWithPermutations(s: String) {
    def distanceOnePermutations: Iterator[String] =
      if (s.isEmpty) Iterator(s)
      else new StringPermutationsIterator(s)
  }

}
