package ua.kpi.ip31.gunawardana.util

import scala.collection.{AbstractIterator, Iterator}
import scala.util.Random

/**
  * @author Ruslan Gunawardana
  */
class RandomPermutationsIterator(original: String, private var count: Int) extends AbstractIterator[String] {
  if (count < 0)
    throw new IllegalArgumentException("count should be >= 0")

  override def hasNext: Boolean = count != 0

  override def next(): String = {
    if (!hasNext)
      Iterator.empty.next()
    count += 1
    val chars = Random shuffle original.toSeq
    chars.mkString
  }
}
