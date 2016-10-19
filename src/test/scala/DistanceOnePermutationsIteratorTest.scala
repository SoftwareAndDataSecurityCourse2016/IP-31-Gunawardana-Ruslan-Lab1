import org.scalatest._
import ua.kpi.ip31.gunawardana.util.DistanceOnePermutationsIterator

/**
  * @author Ruslan Gunawardana
  */
class DistanceOnePermutationsIteratorTest extends FlatSpec with Matchers {
  "DistanceOnePermutationsIterator" should "generate permutations" in {
    val expected = Seq("abcd", "bacd", "abdc", "badc", "acbd")
    val actual = new DistanceOnePermutationsIterator("abcd").toSeq
    actual should contain theSameElementsAs expected
  }
}

