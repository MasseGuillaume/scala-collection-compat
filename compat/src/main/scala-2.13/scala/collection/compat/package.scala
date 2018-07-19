package scala.collection

import scala.collection.{immutable => i, mutable => m}

package object compat {
  type Factory[-A, +C] = scala.collection.Factory[A, C]
  val  Factory         = scala.collection.Factory

  type BuildFrom[-From, -A, +C] = scala.collection.BuildFrom[From, A, C]
  val  BuildFrom                = scala.collection.BuildFrom

  type IterableOnce[+X] = scala.collection.IterableOnce[X]
  val  IterableOnce     = scala.collection.IterableOnce

  implicit class TraversableOnceKVExtensionMethods[K, V](private val self: IterableOnce[(K, V)]) extends AnyVal {
    def toImmutableSortedMap(implicit ordering: Ordering[K]): i.SortedMap[K, V] =
      i.SortedMap.from(self)

    def toImmutableHashMap: i.HashMap[K, V] =
      i.HashMap.from(self)

    def toImmutableListMap: i.ListMap[K, V] =
      i.ListMap.from(self)

    def toImmutableTreeMap(implicit ordering: Ordering[K]): i.TreeMap[K, V] =
      i.TreeMap.from(self)

    def toMutableSortedMap(implicit ordering: Ordering[K]): m.SortedMap[K, V] =
      m.SortedMap.from(self)

    def toMutableHashMap: m.HashMap[K, V] =
      m.HashMap.from(self)

    def toMutableListMap: m.ListMap[K, V] =
      m.ListMap.from(self)

    def toMutableTreeMap(implicit ordering: Ordering[K]): m.TreeMap[K, V] =
      m.TreeMap.from(self)

    def toMutableMap: m.Map[K, V] =
      m.Map.from(self)
  }

  implicit class TraversableOnceIntVExtensionMethods[V](private val self: TraversableOnce[(Int, V)]) extends AnyVal {
    def toImmutableIntMap: i.IntMap[V] =
      i.IntMap.from(self)
  }

  implicit class TraversableOnceLongVExtensionMethods[V](private val self: TraversableOnce[(Long, V)]) extends AnyVal {
    def toImmutableLongMap: i.LongMap[V] =
      i.LongMap.from(self)

    def toMutableLongMap: m.LongMap[V] =
      m.LongMap.from(self)
  }
}
