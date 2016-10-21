package ua.kpi.ip31.gunawardana.repository

/**
  * Thread-safe. Immutable.
  *
  * @author Ruslan Gunawardana
  */
class CiphertextRepository {
  lazy val ciphertexts = ciphertextPaths map { path =>
    val stream = getClass getResourceAsStream path
    io.Source.fromInputStream(stream).mkString
  }
  private[this] val ciphertextPathRoot = "/ciphertext/"
  private[this] val ciphertextPaths = Seq("1.txt", "2.txt", "3.txt", "4.txt") map (ciphertextPathRoot + _)
}
