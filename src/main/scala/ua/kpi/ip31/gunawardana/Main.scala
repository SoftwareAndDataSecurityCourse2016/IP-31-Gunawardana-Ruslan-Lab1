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
    ciphertextRepository.ciphertexts.par foreach { text =>
      val decoded = decoder.decode(text)
      println("Text:")
      println(text)
      println("Decoded:")
      println(decoded)
    }
  }
}
