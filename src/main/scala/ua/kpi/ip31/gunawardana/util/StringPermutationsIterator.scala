package ua.kpi.ip31.gunawardana.util

import scala.collection.{AbstractIterator, Iterator}

/**
  * Performs only neighbour elements permutations.
  */
class StringPermutationsIterator(original: String) extends AbstractIterator[String] {
  private[this] var elms = original.toArray
  private[this] var iterationNum = 1
  private[this] var hasEvenPairsToSwap = true
  private[this] var _hasNext = true

  def hasNext = _hasNext

  def next(): String = {
    if (!hasNext)
      Iterator.empty.next()

    val result = elms.mkString
    if (hasEvenPairsToSwap && !prepareNext()) {
      hasEvenPairsToSwap = false
      iterationNum = 1
    } else {
      _hasNext = prepareNext()
    }
    iterationNum += 1
    result
  }

  private def prepareNext() = {
    elms = original.toArray
    var swapped = false
    var i = iterationNum
    var j = if (hasEvenPairsToSwap) 0 else 1

    while (i != 0) {
      if (i % 2 == 1 && j + 1 < elms.length) {
        swapped = true
        swap(j, j + 1)
      }
      i >>>= 1
      j += 2
    }
    swapped
  }

  private def swap(i: Int, j: Int) {
    val tmpE = elms(i)
    elms(i) = elms(j)
    elms(j) = tmpE
  }
}
