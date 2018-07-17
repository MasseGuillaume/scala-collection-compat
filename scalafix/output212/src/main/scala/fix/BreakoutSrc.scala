


package fix

import scala.collection.compat._

object BreakoutSrc {
  val xs = List(1, 2, 3)

  xs.iterator.collect{ case x => x }.to(scala.collection.immutable.Set): Set[Int]
  xs.iterator.flatMap(x => List(x)).to(scala.collection.SortedSet): collection.SortedSet[Int]
  xs.iterator.map(_ + 1).to(scala.collection.immutable.Set): Set[Int]
  xs.reverseIterator.map(_ + 1).to(scala.collection.immutable.Set): Set[Int]
  xs.iterator.scanLeft(0)((a, b) => a + b).to(scala.collection.immutable.Set): Set[Int]
  xs.view.updated(0, 1).to(scala.collection.immutable.Set): Set[Int]

  (xs.iterator ++ xs).to(scala.collection.immutable.Set): Set[Int]
  (1 +: xs.view).to(scala.collection.immutable.Set): Set[Int]
  (xs.view :+ 1).to(scala.collection.immutable.Set): Set[Int]
  (xs ++: xs.view).to(scala.collection.immutable.Set): Set[Int]

  xs.iterator.concat(xs).to(scala.collection.immutable.Set): Set[Int]
  xs.iterator.zip(xs.iterator).toMap: Map[Int, Int]
  xs.iterator.zipAll(xs.iterator, 0, 0).to(scala.Array): Array[(Int, Int)]
}

