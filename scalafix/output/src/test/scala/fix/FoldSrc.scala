


package fix

import org.junit.Assert._
import org.junit.Test

class FoldSrc {

  @Test
  def dominoes(): Unit = {
    val xs = List(1, 2, 3, 4)

    val f: (Int, Int) => Int = (x, y) => x + y
    val g: (Int, Int) => Int = (x, y) => x * y

    val left = xs.foldLeft(0)(f)
    assertEquals(left, 10)

    val right = xs.foldRight(1)(g)
    assertEquals(right, 24)
  }
}