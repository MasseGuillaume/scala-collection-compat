


package fix

import scala.collection.compat._
object UnsortedSrc {
  import Data._

  cSet.map(ordered)
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

  mBitSet.unsortedSpecific.map(unordered)
  mBitSet.map(ordered)
  mSortedSet.unsortedSpecific.map(unordered)
  mSortedSet.map(ordered)
  mTreeSet.unsortedSpecific.map(unordered)
  mTreeSet.map(ordered)

  // 2.12 ok, 2.13 failure
  // mBitSet -- List(1)
  // mBitSet.flatMap(x => List(x))

  mBitSet ++ List("a") // wtf
  mBitSet.scan("a")((x, y) => (x, y)) // wtf
  // scanLe
  // scanRight
  // toMap

  cSortedMap -- List(1)
}
