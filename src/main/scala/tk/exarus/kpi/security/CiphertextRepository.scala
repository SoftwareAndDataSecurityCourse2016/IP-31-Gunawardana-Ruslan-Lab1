package tk.exarus.kpi.security

import scala.io.Source.fromResource

/**
  * Thread-safe. Immutable.
  *
  * @author Ruslan Gunawardana
  */
class CiphertextRepository {
  import CiphertextRepository.ciphertextPaths

  def ciphertext(num: Int): String = fromResource(ciphertextPaths(num)).mkString
}

object CiphertextRepository {
  private val ciphertextPaths = Seq("1.txt", "2.txt", "3.txt", "4.txt") map ("ciphertext/" + _)
}
