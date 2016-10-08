package ua.kpi.ip31.gunawardana

import ua.kpi.ip31.gunawardana.repository.TextStatisticsRepository
import ua.kpi.ip31.gunawardana.util.DistanceOnePermutationsIterator

import scala.collection.{Iterator}

/**
  * Thread-safe. Immutable.
  *
  * @author Ruslan Gunawardana
  */
class FrequencyDecoder(statsRepo: TextStatisticsRepository, statsCalculator: StatisticsCalculator) extends Decoder {

  import util._

  override def decode(ciphertext: String): String = {
    val lowerCipherKey = findCipherKeyFor(ciphertext.toLowerCase)
    val upperCipherKey = lowerCipherKey map { case (k, v) => k.toUpper -> v.toUpper }
    val cipherKey = (lowerCipherKey ++ upperCipherKey).toMap
    ciphertext map cipherKey
  }

  private def findCipherKeyFor(ciphertext: String): Seq[(Char, Char)] = {
    val expectedFrequencyMap = statsRepo.thirdOrderStatistics
    val ciphertextFrequencyMap = statsCalculator(ciphertext, 3)
    val alphabetSortedByExpectedStats = statsRepo.alphabetSortedByStats(statsRepo.firstOrderStatistics)
    val alphabetSortedByCiphertextStats = statsRepo.alphabetSortedByStats(statsCalculator(ciphertext, 1))

    val alphabetChiSquares = alphabetSortedByCiphertextStats.distanceOnePermutations map { substitutionAlphabet =>
      val decipheringKey = (substitutionAlphabet zip alphabetSortedByExpectedStats).toMap
      val actualFrequencyMap = ciphertextFrequencyMap map { case (k, v) =>
        (k map decipheringKey) -> v
      }
      val chiSquare = statsCalculator.chiSquareTest(expectedFrequencyMap, actualFrequencyMap)
      substitutionAlphabet -> chiSquare
    }
    val sortedSubstitutionAlphabet = (alphabetChiSquares minBy (_._2))._1
    sortedSubstitutionAlphabet zip alphabetSortedByExpectedStats
  }

}
