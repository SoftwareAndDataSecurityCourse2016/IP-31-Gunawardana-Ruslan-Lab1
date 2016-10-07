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
    val text1 = ciphertextRepository.ciphertexts.head
    val decoded = decoder.decode(text1)
    println("Text:")
    println(text1)
    println("Decoded:")
    println(decoded)
  }
}
