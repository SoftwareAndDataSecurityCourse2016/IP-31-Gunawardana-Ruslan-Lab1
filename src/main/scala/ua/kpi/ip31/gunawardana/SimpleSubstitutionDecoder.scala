package ua.kpi.ip31.gunawardana

import com.typesafe.scalalogging.LazyLogging
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
                                cycles: Int) extends Decoder with LazyLogging {
  override def decode(ciphertext: String): String = {
    val lowerDecipheringKey = decipheringKeyFor(ciphertext.toLowerCase)
    val upperDecipheringKey = lowerDecipheringKey map { case (k, v) => k.toUpper -> v.toUpper }
    val decipheringKey = lowerDecipheringKey ++ upperDecipheringKey
    ciphertext map decipheringKey
  }

  private def decipheringKeyFor(ciphertext: String): Map[Char, Char] = {
    val expectedStats = statsRepo.secondOrderStatistics
    val ciphertextStats = statsCalculator.orderStatistics(ciphertext, 2)
    val plaintextAlphabet = statsRepo alphabetSortedByStats statsRepo.firstOrderStatistics
    val ciphertextAlphabet = statsRepo alphabetSortedByStats (statsCalculator firstOrderStatistics ciphertext)

    // least test result is the best
    val decipheringKey = mutable.Map(ciphertextAlphabet zip plaintextAlphabet: _*)
    var minTestResult = testResult(expectedStats, ciphertextStats, decipheringKey)
    var iterationsLeft = cycles
    while (iterationsLeft > 0) {
      val c1 = randomChar(statsRepo.alphabet)
      val c2 = randomChar(statsRepo.alphabet)

      val tmp = decipheringKey(c1)
      decipheringKey(c1) = decipheringKey(c2)
      decipheringKey(c2) = tmp

      val newTestResult = testResult(expectedStats, ciphertextStats, decipheringKey)
      if (newTestResult < minTestResult) {
        minTestResult = newTestResult
        iterationsLeft = cycles
        logger.debug(s"key = $decipheringKey, testResult = $minTestResult")
      } else {
        decipheringKey(c2) = decipheringKey(c1)
        decipheringKey(c1) = tmp
        iterationsLeft -= 1
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
