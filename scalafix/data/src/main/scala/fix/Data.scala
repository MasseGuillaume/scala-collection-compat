package fix

import scala.{collection => c}
import scala.collection.{immutable => i, mutable => m}

case class NoOrdering(v: Int)

object Data {
  val ordered: Int => Int = x => x
  val orderedMap: ((Int, Int)) => ((Int, Int)) = x => x

  val unordered: Int => NoOrdering = x => NoOrdering(x)
  val unorderedMap: ((Int, Int)) => (NoOrdering, Int) = { case (k, v) => (NoOrdering(k), v)}

  val cSet: c.Set[Int] = ???
  val cMap: c.Map[Int, Int] = ???

  val cBitSet    : c.BitSet              = ???
  val cSortedMap : c.SortedMap[Int, Int] = ???
  val cSortedSet : c.SortedSet[Int]      = ???

  val iBitSet    : i.BitSet              = ???
  val iSortedMap : i.SortedMap[Int, Int] = ???
  val iSortedSet : i.SortedSet[Int]      = ???
  val iTreeMap   : i.TreeMap[Int, Int]   = ???
  val iTreeSet   : i.TreeSet[Int]        = ???

  val mBitSet    : m.BitSet              = ???
  val mSortedSet : m.SortedSet[Int]      = ???
  val mTreeSet   : m.TreeSet[Int]        = ???
}
