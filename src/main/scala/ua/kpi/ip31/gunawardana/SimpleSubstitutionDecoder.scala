package ua.kpi.ip31.gunawardana

import ua.kpi.ip31.gunawardana.repository.TextStatisticsRepository

import scala.collection.mutable
import scala.util.Random

/**
  * Thread-safe. Immutable.
  *
  * @author Ruslan Gunawardana
  */
class SimpleSubstitutionDecoder(statsRepo: TextStatisticsRepository,
                                statsCalculator: StatisticsCalculator,
                                cycles: Int) extends Decoder {
  override def decode(ciphertext: String): String = {
    val lowerDecipheringKey = decipheringKeyFor(ciphertext.toLowerCase)
    val upperDecipheringKey = lowerDecipheringKey map { case (k, v) => k.toUpper -> v.toUpper }
    val decipheringKey = lowerDecipheringKey ++ upperDecipheringKey
    ciphertext map decipheringKey
  }

  private def decipheringKeyFor(ciphertext: String): Map[Char, Char] = {
    val expectedStats = statsRepo.thirdOrderStatistics
    val ciphertextStats = statsCalculator.orderStatistics(ciphertext, 3)
    val plaintextAlphabet = statsRepo alphabetSortedByStats statsRepo.firstOrderStatistics
    val ciphertextAlphabet = statsRepo alphabetSortedByStats (statsCalculator firstOrderStatistics ciphertext)

    val decipheringKey = mutable.Map(ciphertextAlphabet zip plaintextAlphabet: _*)
    var minTestResult = testResult(expectedStats, ciphertextStats, decipheringKey)

    // least test result is the best
    for (_ <- 1 to cycles) {
      val c1 = randomChar(statsRepo.alphabet)
      val c2 = randomChar(statsRepo.alphabet)

      val tmp = decipheringKey(c1)
      decipheringKey(c1) = decipheringKey(c2)
      decipheringKey(c2) = tmp

      val newTestResult = testResult(expectedStats, ciphertextStats, decipheringKey)
      if (newTestResult < minTestResult) {
        minTestResult = newTestResult
      } else {
        decipheringKey(c2) = decipheringKey(c1)
        decipheringKey(c1) = tmp
      }
    }
    decipheringKey.toMap
  }

  private def randomChar(str: String): Char = str(Random nextInt str.length)

  private def testResult(expectedStats: Map[String, Double],
                         ciphertextStats: Map[String, Double],
                         decipheringKey: mutable.Map[Char, Char]): Double = {
    val actualStats = ciphertextStats map { case (k, v) =>
      (k map decipheringKey) -> v
    }
    statsCalculator.chiSquareTest(expectedStats, actualStats)
  }
}
