package fix

import scalafix._

final case class CrossCompat(index: SemanticdbIndex) extends SemanticRule(index, "CrossCompat") with Stable212Base
