package tk.exarus.kpi.security

import com.typesafe.scalalogging.LazyLogging

import scala.collection.mutable
import scala.math.log10
import scala.util.Random

/**
  * Thread-safe. Immutable.
  *
  * @author Ruslan Gunawardana
  */
class SimpleSubstitutionDecoder(statsRepo: TextStatisticsRepository, statsCalculator: StatisticsCalculator,
                                cycles: Int)
  extends Decoder with LazyLogging {
  private[this] val random = new Random

  override def decode(ciphertext: String): String = {
    val lowerDecipheringKey = decipheringKeyFor(ciphertext.toLowerCase)
    val upperDecipheringKey = lowerDecipheringKey map { case (k, v) => k.toUpper -> v.toUpper }
    val decipheringKey = lowerDecipheringKey ++ upperDecipheringKey
    ciphertext map decipheringKey
  }

  private def decipheringKeyFor(ciphertext: String): Map[Char, Char] = {
    val expectedStats = statsRepo.thirdOrderStatistics
    val ciphertextStats = statsCalculator.orderStatistics(ciphertext, 3)
    val plaintextAlphabet = alphabetSortedByStats(statsRepo.firstOrderStatistics)
    val ciphertextAlphabet = alphabetSortedByStats(statsCalculator firstOrderStatistics ciphertext)

    // least test result is the best
    val decipheringKey = mutable.Map(ciphertextAlphabet zip plaintextAlphabet: _*)
    var bestTestResult = testResult(expectedStats, ciphertextStats, decipheringKey)
    var iterationsLeft = cycles
    while (iterationsLeft > 0) {
      val (c1, c2) = (randomLetter, randomLetter)
      swap(decipheringKey, c1, c2)

      val newTestResult = testResult(expectedStats, ciphertextStats, decipheringKey)
      if (newTestResult > bestTestResult) {
        bestTestResult = newTestResult
        iterationsLeft = cycles
        logger.debug(s"key = $decipheringKey, testResult = $bestTestResult")
      } else {
        swap(decipheringKey, c1, c2)
        iterationsLeft -= 1
      }
    }
    decipheringKey.toMap
  }

  private def swap(map: mutable.Map[Char, Char], c1: Char, c2: Char): Unit = {
    val tmp = map(c1)
    map(c1) = map(c2)
    map(c2) = tmp
  }

  private def alphabetSortedByStats(stats: Map[Char, Double]): String = {
    val statsByLetter = statsRepo.alphabet map { c =>
      c -> stats.getOrElse(c, 0.0)
    }
    (statsByLetter sortBy (_._2) map (_._1)).mkString
  }

  private def randomLetter: Char = {
    val alphabet = statsRepo.alphabet
    alphabet(random nextInt alphabet.length)
  }

  private def testResult(expectedStats: Map[String, Double],
                         ciphertextStats: Map[String, Double],
                         decipheringKey: collection.Map[Char, Char]): Double = {
    val actualStats = ciphertextStats map { case (k, v) => (k map decipheringKey) -> v }
    fitnessTest(expectedStats, actualStats)
  }

  private def fitnessTest(expectedValues: Map[String, Double], actualValues: Map[String, Double]): Double = {
    val expectedValuesSum = expectedValues.values.sum
    val deviations = actualValues map { case (key, actual) =>
      val expected = expectedValues.getOrElse(key, 0.01)
      log10(expected / expectedValuesSum) * actual
    }
    deviations.sum
  }

  private def chiSquareTest(expectedValues: Map[String, Double], actualValues: Map[String, Double]): Double = {
    val deviations = expectedValues map { case (key, expected) =>
      val actual = actualValues.getOrElse(key, 0.0)
      (actual - expected) * (actual - expected) / expected
    }
    deviations.sum
  }
}
