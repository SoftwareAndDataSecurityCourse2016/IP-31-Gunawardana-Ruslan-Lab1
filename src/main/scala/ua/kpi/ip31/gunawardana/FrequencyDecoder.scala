package ua.kpi.ip31.gunawardana

import ua.kpi.ip31.gunawardana.repository.TextStatisticsRepository

/**
  * Thread-safe. Immutable.
  *
  * @author Ruslan Gunawardana
  */
class FrequencyDecoder(statisticsRepository: TextStatisticsRepository, statisticsCalculator: StatisticsCalculator) extends Decoder {
  override def decode(ciphertext: String): String = {
    val plaintextAlphabet = statisticsRepository.alphabet
    val ciphertextAlphabet = bestFitSubstitutionAlphabet(ciphertext.toLowerCase)

    val lowercaseCipherKey = plaintextAlphabet zip ciphertextAlphabet
    val uppercaseCipherKey = lowercaseCipherKey map { case (k, v) => k.toUpper -> v.toUpper }
    val cipherKey = (lowercaseCipherKey ++ uppercaseCipherKey).toMap
    ciphertext map cipherKey
  }

  private def bestFitSubstitutionAlphabet(ciphertext: String): String = {
    val expectedFrequencyMap = statisticsRepository.thirdOrderStatistics
    val ciphertextFrequencyMap = statisticsCalculator calculateThirdOrderStatisticsFor ciphertext
    val plaintextAlphabet = statisticsRepository.alphabet

    val alphabetChiSquares = plaintextAlphabet.permutations map { substitutionAlphabet =>
      val cipherKey = (plaintextAlphabet zip substitutionAlphabet).toMap
      val substitutionFrequencyMap = ciphertextFrequencyMap map { case (k, v) => (k map cipherKey) -> v }
      val chiSquare = statisticsCalculator.chiSquareTest(expectedFrequencyMap, substitutionFrequencyMap)
      substitutionAlphabet -> chiSquare
    }
    (alphabetChiSquares reduce ((x, y) => if (x._2 < y._2) x else y))._1
  }
}
