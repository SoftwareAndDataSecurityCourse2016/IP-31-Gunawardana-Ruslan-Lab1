package ua.kpi.ip31.gunawardana

import ua.kpi.ip31.gunawardana.repository.{CiphertextRepository, EnglishTextStatisticsRepository}

/**
  * Starting point of the application
 *
  * @author Ruslan Gunawardana
  */
object Main {
  val decoder = new FrequencyDecoder(new EnglishTextStatisticsRepository, new StatisticsCalculator)
  val ciphertextRepository = new CiphertextRepository

  def main(args: Array[String]): Unit = {
    val ciphertext = ciphertextRepository.ciphertexts.head
    val plaintext = decoder.decode(ciphertext)
    println("Encoded ciphertext:")
    println(ciphertext)
    println("Decoded plaintext:")
    println(plaintext)
  }
}
