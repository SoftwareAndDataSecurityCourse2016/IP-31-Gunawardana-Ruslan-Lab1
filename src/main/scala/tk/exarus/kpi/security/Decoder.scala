package tk.exarus.kpi.security

/**
  * @author Ruslan Gunawardana
  */
trait Decoder {
  def decode(ciphertext: String): String
}
