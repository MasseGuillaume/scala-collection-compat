


package fix

import scala.collection.compat._
object UnsortedSrc {
  import Data._

  cMap.map(orderedMap)

  cBitSet.unsortedSpecific.map(unordered)
  cBitSet.map(ordered)
  cSortedMap.unsortedSpecific.map(unorderedMap)
  cSortedMap.map(orderedMap)
  cSortedSet.unsortedSpecific.map(unordered)
  cSortedSet.map(ordered)

  iBitSet.unsortedSpecific.map(unordered)
  iBitSet.map(ordered)
  iSortedMap.unsortedSpecific.map(unorderedMap)
  iSortedMap.map(orderedMap)
  iSortedSet.unsortedSpecific.map(unordered)
  iSortedSet.map(ordered)
  iTreeMap.unsortedSpecific.map(unorderedMap)
  iTreeMap.map(orderedMap)
  iTreeSet.unsortedSpecific.map(unordered)
  iTreeSet.map(ordered)
}
