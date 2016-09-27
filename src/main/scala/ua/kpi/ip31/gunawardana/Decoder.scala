package ua.kpi.ip31.gunawardana

/**
  * @author Ruslan Gunawardana
  */
trait Decoder {
  def decode(ciphertext: String): String
}
