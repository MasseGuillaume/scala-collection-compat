


package fix

import scala.collection.{immutable, mutable}
import scala.concurrent.Future
import scala.concurrent.ExecutionContext.Implicits.global
import scala.collection.compat._

class BreakoutSrc(ts: Iterable[Int], vec: Vector[Int], list: List[Int], seq: Seq[Int]) {

  // `IndexedSeqOptimized.zip`
  vec.iterator.zip(vec.iterator).toMap: Map[Int, Int]

  // `IterableLike.zip`
  seq.iterator.zip(seq.iterator).toMap: Map[Int, Int]

  // `IterableLike.zipAll`
  seq.iterator.zipAll(seq.iterator, 0, 0).to(scala.Array): Array[(Int, Int)]

  // `List ++`
  (list.iterator ++ list).to(scala.collection.immutable.Set): Set[Int]

  // `List +:`
  (1 +: list.view).to(scala.collection.immutable.Set): Set[Int]

  // `List.collect`
  list.iterator.collect{ case x => x}.to(scala.collection.immutable.Set): Set[Int]

  // `List.flatMap`
  list.iterator.flatMap(x => List(x)).to(scala.collection.immutable.Set): Set[Int]

  // `List.map`
  list.iterator.map(x => x).to(scala.collection.immutable.Set): Set[Int]


  // `SeqLike.reverseMap`
  seq.reverseIterator.map(_ + 1).to(scala.collection.immutable.Set): Set[Int]

  // `SeqLike +:`
  (1 +: seq.view).to(scala.collection.immutable.List): List[Int]

  // `SeqLike :+`
  (seq.view :+ 1).to(scala.collection.immutable.List): List[Int]

  // `SeqLike.updated`
  (seq.view.updated(0, 0)).to(scala.collection.immutable.List): List[Int]

  // `SeqLike.union`
  seq.iterator.concat(seq).to(scala.collection.immutable.List): List[Int]


  //`SetLike.map`
  Set(1).iterator.map(x => x).to(scala.collection.immutable.List): List[Int]


  // `TraversableLike ++`
  (ts.iterator ++ ts ).to(scala.collection.immutable.Set): Set[Int]

  // `TraversableLike ++:`
  (ts ++: ts.view).to(scala.collection.immutable.Set): Set[Int]

  // `TraversableLike.collect`
  ts.iterator.collect{ case x => x }.to(scala.collection.immutable.Set): Set[Int]

  // `TraversableLike.flatMap`
  ts.iterator.flatMap(x => List(x)).to(scala.collection.SortedSet): collection.SortedSet[Int]

  // `TraversableLike.map`
  ts.iterator.map(_ + 1).to(scala.collection.immutable.Set): Set[Int]

  // `TraversableLike.scanLeft`
  ts.iterator.scanLeft(0)((a, b) => a + b).to(scala.collection.immutable.Set): Set[Int]


  // `Vector ++`
  (vec.iterator ++ List(1)).to(scala.collection.immutable.List): List[Int]

  // `Vector +:`
  (1 +: vec.view).to(scala.collection.immutable.List): List[Int]

  // `Vector :+`
  (vec.view :+ 1).to(scala.collection.immutable.List): List[Int]

  // `Vector.updated`
  (vec.view.updated(0, 0)).to(scala.collection.immutable.List): List[Int]

  Future.sequence(List(Future(1)))(scala.collection.immutable.List, global): Future[Seq[Int]]
  Future.traverse(List(1))(x => Future(x))(scala.collection.immutable.List, global): Future[Seq[Int]]
}
