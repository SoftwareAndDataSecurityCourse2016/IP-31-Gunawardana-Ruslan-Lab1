package ua.kpi.ip31.gunawardana

import scala.collection.Iterator

/**
  * @author Ruslan Gunawardana
  */
package object util {

  implicit class StringWithPermutations(s: String) {
    def distanceOnePermutations: Iterator[String] =
      if (s.isEmpty) Iterator(s)
      else new DistanceOnePermutationsIterator(s)

    def distancePermutations(distance: Int): Iterator[String] =
      if (s.isEmpty) Iterator(s)
      else new DistancePermutationsIterator(s, distance)

    def randomPermutations(count: Int): Iterator[String] =
      if (s.isEmpty) Iterator(s)
      else new RandomPermutationsIterator(s, count)
  }

}
