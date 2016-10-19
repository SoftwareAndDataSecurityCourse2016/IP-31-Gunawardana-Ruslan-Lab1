package ua.kpi.ip31.gunawardana

import ua.kpi.ip31.gunawardana.repository.{CiphertextRepository, EnglishTextStatisticsRepository}

import scala.util.Try

/**
  * Starting point of the application
  *
  * @author Ruslan Gunawardana
  */
object Main extends App {
  val cycles = Try(args(0).toInt) getOrElse 1024 * 64

  val statsRepo = new EnglishTextStatisticsRepository
  val statsCalculator = new StatisticsCalculator
  val decoder = new SimpleSubstitutionDecoder(statsRepo, statsCalculator, cycles)
  val ciphertextRepository = new CiphertextRepository

  val ciphertext = ciphertextRepository.ciphertexts.head
  val plaintext = decoder.decode(ciphertext)
  println("Encoded ciphertext:")
  println(ciphertext)
  println("Decoded plaintext:")
  println(plaintext)
}
