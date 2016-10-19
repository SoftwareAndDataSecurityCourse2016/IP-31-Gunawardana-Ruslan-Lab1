package ua.kpi.ip31.gunawardana.util

import scala.collection.AbstractIterator

/**
  * @author Ruslan Gunawardana
  */
class DistancePermutationsIterator(original: String, distance: Int) extends AbstractIterator[String] {
  private[this] val elms = original.toArray
  private[this] var _hasNext = true

  override def hasNext: Boolean = _hasNext

  var i = 1

  override def next(): String = {
    val result = elms.mkString
    if (i % 1000 == 0) println(i)
    if (!prepareNext()) _hasNext = false
    i += 1
    result
  }

  private def prepareNext(): Boolean = {
    // Find longest non-increasing suffix
    var i = elms.length - 1
    while (i > 0 && elms(i - 1) >= elms(i))
      i -= 1

    val isNotLastPermutation = i > 0
    var j = elms.length - 1
    if (isNotLastPermutation) {
      // Let elms(i - 1) be the pivot. Find rightmost element that exceeds the pivot and swap
      while (elms(j) <= elms(i - 1))
        j -= 1
      swap(i - 1, j)

      // Reverse the suffix
      j = elms.length - 1
      while (i < j) {
        swap(i, j)
        i += 1
        j -= 1
      }
    }
    isNotLastPermutation
  }

  private def swap(i: Int, j: Int) {
    val tmpE = elms(i)
    elms(i) = elms(j)
    elms(j) = tmpE
  }
}
