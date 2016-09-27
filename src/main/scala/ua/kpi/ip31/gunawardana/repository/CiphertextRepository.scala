package ua.kpi.ip31.gunawardana.repository

/**
  * Thread-safe. Immutable.
  *
  * @author Ruslan Gunawardana
  */
class CiphertextRepository {
  private val ciphertextPathRoot = "/ciphertext/"
  private val ciphertextPaths = Seq("1.txt", "2.txt", "3.txt", "4.txt") map (ciphertextPathRoot + _)
  lazy val ciphertexts = ciphertextPaths map { path =>
    io.Source.fromInputStream(getClass.getResourceAsStream(path)).mkString
  }
}
