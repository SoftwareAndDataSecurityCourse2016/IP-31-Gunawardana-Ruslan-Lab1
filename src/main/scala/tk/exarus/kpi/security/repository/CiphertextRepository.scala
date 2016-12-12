package tk.exarus.kpi.security.repository

import scala.io.Source.fromResource

/**
  * Thread-safe. Immutable.
  *
  * @author Ruslan Gunawardana
  */
class CiphertextRepository {
  import CiphertextRepository.ciphertextPaths

  def ciphertext(num: Int) = {
    val path = ciphertextPaths(num)
    fromResource(path).mkString
  }
}

object CiphertextRepository {
  private val ciphertextPaths = {
    val ciphertextPathRoot = "ciphertext/"
    Seq("1.txt", "2.txt", "3.txt", "4.txt") map (ciphertextPathRoot + _)
  }
}
