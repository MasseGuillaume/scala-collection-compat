package fix

import scalafix._
import scalafix.util._
import scala.meta._

import scala.collection.mutable

trait CrossCompatibility {
  def isCrossCompatible: Boolean
}

// 2.12 Cross-Compatible
trait Stable212Base extends CrossCompatibility { self: SemanticRule =>

  // Two rules triggers the same rewrite TraversableLike.to and CanBuildFrom
  // we keep track of what is handled in CanBuildFrom and guard against TraversableLike.to
  val handledTo = mutable.Set[Tree]()

  //  == Symbols ==
  def foldSymbol(isLeft: Boolean): SymbolMatcher = {
    val op =
      if (isLeft) "/:"
      else ":\\"

    normalized(s"_root_.scala.collection.TraversableOnce.`$op`.")
  }

  val iterator = normalized("_root_.scala.collection.TraversableLike.toIterator.")
  val toTpe = normalized("_root_.scala.collection.TraversableLike.to.")
  val copyToBuffer = normalized("_root_.scala.collection.TraversableOnce.copyToBuffer.")
  val arrayBuilderMake = normalized("_root_.scala.collection.mutable.ArrayBuilder.make(Lscala/reflect/ClassTag;)Lscala/collection/mutable/ArrayBuilder;.")
  val iterableSameElement = exact("_root_.scala.collection.IterableLike#sameElements(Lscala/collection/GenIterable;)Z.")
  val collectionCanBuildFrom = exact("_root_.scala.collection.generic.CanBuildFrom#")
  val collectionCanBuildFromImport = exact("_root_.scala.collection.generic.CanBuildFrom.;_root_.scala.collection.generic.CanBuildFrom#")
  val nothing = exact("_root_.scala.Nothing#")
  val setPlus2 = exact("_root_.scala.collection.SetLike#`+`(Ljava/lang/Object;Ljava/lang/Object;Lscala/collection/Seq;)Lscala/collection/Set;.")
  val mapPlus2 = exact("_root_.scala.collection.immutable.MapLike#`+`(Lscala/Tuple2;Lscala/Tuple2;Lscala/collection/Seq;)Lscala/collection/immutable/Map;.")
  val mutSetPlus = exact("_root_.scala.collection.mutable.SetLike#`+`(Ljava/lang/Object;)Lscala/collection/mutable/Set;.")
  val mutMapPlus = exact("_root_.scala.collection.mutable.MapLike#`+`(Lscala/Tuple2;)Lscala/collection/mutable/Map;.")
  val mutMapUpdate = exact("_root_.scala.collection.mutable.MapLike#updated(Ljava/lang/Object;Ljava/lang/Object;)Lscala/collection/mutable/Map;.")
  val foldLeftSymbol = foldSymbol(isLeft = true)
  val foldRightSymbol = foldSymbol(isLeft = false)

  val traversable = exact(
    "_root_.scala.package.Traversable#",
    "_root_.scala.collection.Traversable#",
  )

  object Breakout {
    implicit class RichSymbol(val symbol: Symbol) {
      def exact(tree: Tree)(implicit index: SemanticdbIndex): Boolean =
        index.symbol(tree).fold(false)(_ == symbol)
    }

    val breakOut = SymbolMatcher.exact(Symbol("_root_.scala.collection.package.breakOut(Lscala/collection/generic/CanBuildFrom;)Lscala/collection/generic/CanBuildFrom;."))

    // infix operators
    val `List ++`             = Symbol("_root_.scala.collection.immutable.List#`++`(Lscala/collection/GenTraversableOnce;Lscala/collection/generic/CanBuildFrom;)Ljava/lang/Object;.")
    val `List +:`             = Symbol("_root_.scala.collection.immutable.List#`+:`(Ljava/lang/Object;Lscala/collection/generic/CanBuildFrom;)Ljava/lang/Object;.")
    val `SeqLike :+`          = Symbol("_root_.scala.collection.SeqLike#`:+`(Ljava/lang/Object;Lscala/collection/generic/CanBuildFrom;)Ljava/lang/Object;.")
    val `TraversableLike ++:` = Symbol("_root_.scala.collection.TraversableLike#`++:`(Lscala/collection/Traversable;Lscala/collection/generic/CanBuildFrom;)Ljava/lang/Object;.")

    val operatorsIteratorSymbols = List(`List ++`)
    val operatorsViewSymbols     = List(`List +:`, `SeqLike :+`, `TraversableLike ++:`)
    val operatorsSymbols         = operatorsViewSymbols ++ operatorsIteratorSymbols

    val operatorsIterator = SymbolMatcher.exact(operatorsIteratorSymbols: _*)
    val operatorsView     = SymbolMatcher.exact(operatorsViewSymbols: _*)
    val operators         = SymbolMatcher.exact(operatorsSymbols: _*)

    // select
    val `List.collect`        = Symbol("_root_.scala.collection.immutable.List#collect(Lscala/PartialFunction;Lscala/collection/generic/CanBuildFrom;)Ljava/lang/Object;.")
    val `List.flatMap`        = Symbol("_root_.scala.collection.immutable.List#flatMap(Lscala/Function1;Lscala/collection/generic/CanBuildFrom;)Ljava/lang/Object;.")
    val `List.map`            = Symbol("_root_.scala.collection.immutable.List#map(Lscala/Function1;Lscala/collection/generic/CanBuildFrom;)Ljava/lang/Object;.")
    val `IterableLike.zip`    = Symbol("_root_.scala.collection.IterableLike#zip(Lscala/collection/GenIterable;Lscala/collection/generic/CanBuildFrom;)Ljava/lang/Object;.")
    val `IterableLike.zipAll` = Symbol("_root_.scala.collection.IterableLike#zipAll(Lscala/collection/GenIterable;Ljava/lang/Object;Ljava/lang/Object;Lscala/collection/generic/CanBuildFrom;)Ljava/lang/Object;.")
    val `SeqLike.union`       = Symbol("_root_.scala.collection.SeqLike#union(Lscala/collection/GenSeq;Lscala/collection/generic/CanBuildFrom;)Ljava/lang/Object;.")
    val `SeqLike.updated`     = Symbol("_root_.scala.collection.SeqLike#updated(ILjava/lang/Object;Lscala/collection/generic/CanBuildFrom;)Ljava/lang/Object;.")
    val `SeqLike.reverseMap`  = Symbol("_root_.scala.collection.SeqLike#reverseMap(Lscala/Function1;Lscala/collection/generic/CanBuildFrom;)Ljava/lang/Object;.")

    val functionsIteratorSymbols        = List(`List.collect`, `List.flatMap`, `List.map`, `IterableLike.zip`, `IterableLike.zipAll`, `SeqLike.union`)
    val functionsViewSymbols            = List(`SeqLike.updated`)
    val functionsReverseIteratorSymbols = List(`SeqLike.reverseMap`)
    val functionsSymbols                = functionsIteratorSymbols ++ functionsViewSymbols ++ functionsReverseIteratorSymbols

    val functionsIterator        = SymbolMatcher.exact(functionsIteratorSymbols: _*)
    val functionsReverseIterator = SymbolMatcher.exact(functionsReverseIteratorSymbols: _*)
    val functionsView            = SymbolMatcher.exact(functionsViewSymbols: _*)
    val functions                = SymbolMatcher.exact(functionsSymbols: _*)

    val functionsZip             = SymbolMatcher.exact(`IterableLike.zip`, `IterableLike.zipAll`)

    // special select

    // iterator
    val `TraversableLike.scanLeft` = SymbolMatcher.exact(Symbol("_root_.scala.collection.TraversableLike#scanLeft(Ljava/lang/Object;Lscala/Function2;Lscala/collection/generic/CanBuildFrom;)Ljava/lang/Object;."))

    def isLeftAssociative(tree: Tree): Boolean =
      tree match {
        case Term.Name(value) => value.last != ':'
        case _ => false
      }
  }


  // == Rules ==
  def replaceBreakout(ctx: RuleCtx): Patch = {
    import Breakout._

    def fixIt(intermediateLhs: String,
              lhs: Term,
              ap: Term,
              breakout: Tree,
              ap0: Term,
              intermediateRhs: Option[String] = None,
              rhs: Option[Term] = None): Patch = {

      val toCollection = extractColFromBreakout(breakout).syntax

      val patchRhs =
        (intermediateRhs, rhs) match {
          case (Some(i), Some(r)) => ctx.addRight(r, "." + i)
          case _ => Patch.empty
        }

      val patchSpecificCollection =
        toCollection match {
          case "scala.collection.immutable.Map" =>
            ctx.addRight(ap0, ".toMap")

          case _ =>
            Patch.empty
        }

      val sharedPatch =
        ctx.addRight(lhs, "." + intermediateLhs) +
        patchRhs

      val toColl =
        if (patchSpecificCollection.isEmpty) {
          ctx.addRight(ap, ".to") +
          ctx.replaceTree(breakout, toCollection)
        } else {
          val breakoutWithParens = ap0.tokens.slice(ap.tokens.size, ap0.tokens.size)

          ctx.removeTokens(breakoutWithParens) +
          patchSpecificCollection
        }

      sharedPatch + toColl
    }

    def extractColFromBreakout(breakout: Tree): Term = {
      val synth = ctx.index.synthetics.find(_.position.end == breakout.pos.end).get
      val Term.Apply(_, List(implicitCbf)) = synth.text.parse[Term].get

      implicitCbf match {
        case Term.ApplyType(Term.Select(coll,_), _) => coll
        case Term.Apply(Term.ApplyType(Term.Select(coll, _), _), _) => coll
        case _ => {
          throw new Exception(
            s"""|cannot extract breakout collection:
                |
                |---------------------------------------------
                |syntax:
                |${implicitCbf.syntax}
                |
                |---------------------------------------------
                |structure:
                |${implicitCbf.structure}""".stripMargin
          )
        }
      }
    }

    val rewriteBreakout =
      ctx.tree.collect {
        case i: Importee if breakOut.matches(i) =>
          ctx.removeImportee(i)

        case ap0 @ Term.Apply(ap @ Term.ApplyInfix(lhs, operators(op), _, List(rhs)), List(breakOut(bo))) =>
          val subject =
            if(isLeftAssociative(op)) lhs
            else rhs

          val intermediate =
            op match {
              case operatorsIterator(_) => "iterator"
              case operatorsView(_)     => "view"
              // since operators(op) matches iterator and view
              case _                    => throw new Exception("impossible")
            }

          fixIt(intermediate, subject, ap, bo, ap0)

        case ap0 @ Term.Apply(ap @ Term.Apply(Term.Select(lhs, functions(op)), rhs :: _), List(breakOut(bo))) =>

          val intermediateLhs =
            op match {
              case functionsIterator(_)        => "iterator"
              case functionsView(_)            => "view"
              case functionsReverseIterator(_) => "reverseIterator"
              // since functions(op) matches iterator, view and reverseIterator
              case _                           => throw new Exception("impossible")
            }

          val intermediateRhs =
            op match {
              case functionsZip(_) => Some("iterator")
              case _               => None
            }

          val replaceUnion =
            if (`SeqLike.union`.exact(op)) ctx.replaceTree(op, "concat")
            else Patch.empty

          val isReversed = `SeqLike.reverseMap`.exact(op)
          val replaceReverseMap =
            if (isReversed) ctx.replaceTree(op, "map")
            else Patch.empty

          fixIt(intermediateLhs, lhs, ap, bo, ap0, intermediateRhs, Some(rhs)) + replaceUnion + replaceReverseMap

        case ap0 @ Term.Apply(ap @ Term.Apply(Term.Apply(Term.Select(lhs, `TraversableLike.scanLeft`(op)), _), _), List(breakOut(bo))) =>
          fixIt("iterator", lhs, ap, bo, ap0)
      }.asPatch

    val compatImport =
      if(rewriteBreakout.nonEmpty) addCompatImport(ctx)
      else Patch.empty

    rewriteBreakout + compatImport
  }

  def replaceIterableSameElements(ctx: RuleCtx): Patch = {
    val sameElements =
      ctx.tree.collect {
        case Term.Apply(Term.Select(lhs, iterableSameElement(_)), List(_)) =>
          ctx.addRight(lhs, ".iterator")
      }.asPatch

    val compatImport =
      if(sameElements.nonEmpty) addCompatImport(ctx)
      else Patch.empty

    sameElements + compatImport
  }


  def replaceSymbols0(ctx: RuleCtx): Patch = {
    val traversableToIterable =
      ctx.replaceSymbols(
        "scala.Traversable"            -> "scala.Iterable",
        "scala.collection.Traversable" -> "scala.collection.Iterable"
      )

    val linearSeqToList =
      ctx.replaceSymbols(
        "scala.collection.LinearSeq" -> "scala.collection.immutable.List",
      )

    import scala.meta.contrib._
    val hasTraversable =
        ctx.tree.exists {
          case traversable(_) => true
          case _ => false

        }

    val compatImport =
      if (hasTraversable) addCompatImport(ctx)
      else Patch.empty

    traversableToIterable + linearSeqToList + compatImport
  }

  def replaceSymbolicFold(ctx: RuleCtx): Patch = {
    ctx.tree.collect {
      case Term.Apply(ap @ Term.ApplyInfix(rhs, foldRightSymbol(_), _, List(lhs)), _) =>
        ctx.replaceTree(ap, s"$rhs.foldRight($lhs)")

      case Term.Apply(ap @ Term.ApplyInfix(lhs, foldLeftSymbol(_), _, List(rhs)), _) =>
        ctx.replaceTree(ap, s"$rhs.foldLeft($lhs)")
    }.asPatch
  }

  def replaceCopyToBuffer(ctx: RuleCtx): Patch = {
    ctx.tree.collect {
      case t @ q"${copyToBuffer(Term.Select(collection, _))}($buffer)" =>
        ctx.replaceTree(t, q"$buffer ++= $collection".syntax)
    }.asPatch
  }

  def replaceSetMapPlus2(ctx: RuleCtx): Patch = {
    def rewritePlus(ap: Term.ApplyInfix, lhs: Term, op: Term.Name, rhs1: Term, rhs2: Term): Patch = {
      val tokensToReplace =
        if(startsWithParens(ap)) {
          // don't drop surrounding parens
          ap.tokens.slice(1, ap.tokens.size - 1)
        } else ap.tokens

      val newTree =
        Term.ApplyInfix(
          Term.ApplyInfix(lhs, op, Nil, List(rhs1)),
          op,
          Nil,
          List(rhs2)
        ).syntax

      ctx.removeTokens(tokensToReplace) +
      tokensToReplace.headOption.map(x => ctx.addRight(x, newTree))
    }
    ctx.tree.collect {
      case ap @ Term.ApplyInfix(lhs, op @ mapPlus2(_), _, List(a, b)) =>
        rewritePlus(ap, lhs, op, a, b)

      case ap @ Term.ApplyInfix(lhs, op @ setPlus2(_), _, List(a, b)) =>
        rewritePlus(ap, lhs, op, a, b)
    }.asPatch
  }

  def replaceMutSetMapPlus(ctx: RuleCtx): Patch = {
    def rewriteMutPlus(lhs: Term, op: Term.Name): Patch = {
      ctx.addRight(lhs, ".clone()") +
      ctx.addRight(op, "=")
    }

    ctx.tree.collect {
      case Term.ApplyInfix(lhs, op @ mutSetPlus(_), _, List(_)) =>
        rewriteMutPlus(lhs, op)

      case Term.ApplyInfix(lhs, op @ mutMapPlus(_), _, List(_)) =>
        rewriteMutPlus(lhs, op)
    }.asPatch
  }

  def replaceMutMapUpdated(ctx: RuleCtx): Patch = {
    ctx.tree.collect {
      case Term.Apply(Term.Select(a, up @ mutMapUpdate(_)), List(k, v)) => {
        ctx.addRight(up, "clone() += (") +
        ctx.removeTokens(up.tokens) +
        ctx.addRight(v, ")")
      }
    }.asPatch
  }

  def replaceArrayBuilderMake(ctx: RuleCtx): Patch = {
    ctx.tree.collect {
      case ap @ Term.Apply(at @ Term.ApplyType(Term.Select(lhs, arrayBuilderMake(_)), args), Nil) =>
        val extraParens =
          ap.tokens.slice(at.tokens.size, ap.tokens.size)
        ctx.removeTokens(extraParens)
    }.asPatch
  }

  def replaceCanBuildFrom(ctx: RuleCtx): Patch = {
    val useSites =
      ctx.tree.collect {
        case Defn.Def(_, _, _, paramss, _, body) =>
          CanBuildFromNothing(paramss, body, ctx, collectionCanBuildFrom, nothing, toTpe, handledTo) +
            CanBuildFrom(paramss, body, ctx, collectionCanBuildFrom, nothing)
      }.asPatch

    val imports =
      ctx.tree.collect {
        case i: Importee if collectionCanBuildFromImport.matches(i) =>
            ctx.removeImportee(i)
      }.asPatch

    val compatImport = addCompatImport(ctx)

    if (useSites.nonEmpty) useSites + imports + compatImport
    else Patch.empty
  }

   def extractCollection(toCol: Tree): String = {
     toCol match {
       case Term.ApplyType(q"scala.Predef.fallbackStringCanBuildFrom", _) =>
         "scala.collection.immutable.IndexedSeq"
       case Term.ApplyType(Term.Select(coll,_), _) =>
         coll.syntax
       case Term.Apply(Term.ApplyType(Term.Select(coll, _), _), _) =>
         coll.syntax
       case Term.Select(coll,_) =>
         coll.syntax
       case _ => {
         throw new Exception(
           s"""|cannot extract collection from .to
               |
               |---------------------------------------------
               |syntax:
               |${toCol.syntax}
               |
               |---------------------------------------------
               |structure:
               |${toCol.structure}""".stripMargin
         )
       }
     }
   }

  def replaceToList(ctx: RuleCtx): Patch = {
    ctx.tree.collect {
      case iterator(t: Name) =>
        ctx.replaceTree(t, "iterator")

      case Term.ApplyType(Term.Select(_, t @ toTpe(n: Name)), _) if !handledTo.contains(n) =>
        trailingBrackets(n, ctx).map { case (open, close) =>
          ctx.replaceToken(open, "(") + ctx.replaceToken(close, ")")
        }.asPatch

      case Term.Select(_, to @ toTpe(_)) =>
        val synth = ctx.index.synthetics.find(_.position.end == to.pos.end)
        synth.map{ s =>
          val Term.Apply(_, List(toCol)) = s.text.parse[Term].get
          val col = extractCollection(toCol)
          ctx.addRight(to, "(" + col + ")")
        }.getOrElse(Patch.empty)
    }.asPatch
  }


  def addCompatImport(ctx: RuleCtx): Patch = {
    if (isCrossCompatible) ctx.addGlobalImport(importer"scala.collection.compat._")
    else Patch.empty
  }

  override def fix(ctx: RuleCtx): Patch = {
    replaceSymbols0(ctx) +
      replaceCanBuildFrom(ctx) +
      replaceToList(ctx) +
      replaceCopyToBuffer(ctx) +
      replaceSymbolicFold(ctx) +
      replaceSetMapPlus2(ctx) +
      replaceMutSetMapPlus(ctx) +
      replaceMutMapUpdated(ctx) +
      replaceArrayBuilderMake(ctx) +
      replaceIterableSameElements(ctx) +
      replaceBreakout(ctx)
  }

}
