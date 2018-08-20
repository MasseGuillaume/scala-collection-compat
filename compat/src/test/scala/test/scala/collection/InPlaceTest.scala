package test.scala.collection

import org.junit.Assert._
import org.junit.Test

import scala.collection.compat._

class InPlaceTest {

  @Test
  def array: Unit = {
    val obtained = Array(1, 2, 3)
    obtained.mapInPlace(_ + 1)

    val expected = Array(2, 3, 4)
    assertArrayEquals(expected, obtained)
  }

  @Test
  def wrapper: Unit = {}
}
