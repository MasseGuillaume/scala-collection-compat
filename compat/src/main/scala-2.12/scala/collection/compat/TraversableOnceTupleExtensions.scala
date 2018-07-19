package scala.collection.compat

import scala.collection.{immutable => i, mutable => m}

class TraversableOnceKVExtensionMethods[K, V](private val self: TraversableOnce[(K, V)]) extends AnyVal {
  def toImmutableSortedMap(implicit ordering: Ordering[K]): i.SortedMap[K, V] = {
    val b = i.SortedMap.canBuildFrom[K, V](ordering)()
    b ++= self
    b.result()
  }

  def toImmutableHashMap: i.HashMap[K, V] = {
    val b = i.HashMap.canBuildFrom[K, V]()
    b ++= self
    b.result()
  }

  def toImmutableListMap: i.ListMap[K, V] = {
    val b = i.ListMap.canBuildFrom[K, V]()
    b ++= self
    b.result()
  }

  def toImmutableTreeMap(implicit ordering: Ordering[K]): i.TreeMap[K, V] = {
    val b = i.TreeMap.canBuildFrom[K, V](ordering)()
    b ++= self
    b.result()
  }

  def toMutableSortedMap(implicit ordering: Ordering[K]): m.SortedMap[K, V] = {
    val b = m.SortedMap.canBuildFrom[K, V](ordering)()
    b ++= self
    b.result()
  }

  def toMutableHashMap: m.HashMap[K, V] = {
    val b = m.HashMap.canBuildFrom[K, V]()
    b ++= self
    b.result()
  }

  def toMutableListMap: m.ListMap[K, V] = {
    val b = m.ListMap.canBuildFrom[K, V]()
    b ++= self
    b.result()
  }

  def toMutableTreeMap(implicit ordering: Ordering[K]): m.TreeMap[K, V] = {
    val b = m.TreeMap.canBuildFrom[K, V](ordering)()
    b ++= self
    b.result()
  }

  def toMutableMap: m.Map[K, V] = {
    val b = m.Map.canBuildFrom[K, V]()
    b ++= self
    b.result()
  }
}
