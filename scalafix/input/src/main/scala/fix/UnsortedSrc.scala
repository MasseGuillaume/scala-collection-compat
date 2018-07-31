/*
rule = "scala:fix.Experimental"
 */
package fix

object UnsortedSrc {
  import Data._

  cMap.map(orderedMap)

  cBitSet.map(unordered)
  cBitSet.map(ordered)
  cSortedMap.map(unorderedMap)
  cSortedMap.map(orderedMap)
  cSortedSet.map(unordered)
  cSortedSet.map(ordered)

  iBitSet.map(unordered)
  iBitSet.map(ordered)
  iSortedMap.map(unorderedMap)
  iSortedMap.map(orderedMap)
  iSortedSet.map(unordered)
  iSortedSet.map(ordered)
  iTreeMap.map(unorderedMap)
  iTreeMap.map(orderedMap)
  iTreeSet.map(unordered)
  iTreeSet.map(ordered)
}

