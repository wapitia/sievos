package org.sievos.kern

/**
 * Part is a simple binary tree representation called here a Partition.
 * Each Part is a node in the binary tree. A node may either be a
 * LeafNode or a SplitNode (non-terminal node).
 * 
 * <p>The creation of the tree is strictly enforced by the Part constructor
 * apply methods and by the extract/prepend/combine operations so that:
 * 
 * <ul>
 * <li>Each LeafNode holds one value.
 * <li>Each SplitNode holds two sub Parts, left and right. 
 *     Both left and right are either a LeafNode or recursively another
 *     SplitNode. They cannot be null or missing.
 * <li>All nodes are immutable. To change a value, a new tree is constructed
 *     as the old tree is traversed via extract/prepend/combine operations.
 * </ul>
 * 
 */
trait Part[+T] {
  
  /**
   * partition context. Each node has an implied position, a list indicating
   * where it resides in its tree. The list is ordered from lowest to top-most 
   * indices. The root node has a corresponding empty context list
   */
  type PCTX = List[Int]
 
  /**
   * operator '|'
   * Makes a new partition of two elements forming (this | that)
   */
  def | [B >: T](that: Part[B]): Part[B]
  
  /**
   * operator '|+'
   * Makes a new partition of two elements forming (this | Part(that))
   */
  def |+ [B >: T](that: B): Part[B]

  /**
   * Visit each partition which can deliver items of type T to
   * the supplied consumer
   */
  def visit(tconsumer: (T,PCTX) => Unit): Unit

  /**
   * Is this partition empty?
   */
  def isEmpty: Boolean
  
  /**
   * Is this partition a leaf?
   */
  def isLeaf: Boolean
  
  /**
   * Is this partition split into at least two partitions?
   */
  def isSplit: Boolean
  
  /**
   * combine is callable only when isSplit is true  
   */
  def combineL[B >: T](tcomb: (B,B) => B): Part[B]
  def combineR[B >: T](tcomb: (B,B) => B): Part[B]
  
  /**
   * extract is callable only when isEmpty is false  
   */
  def extractL: (T,Part[T])
  def extractR: (Part[T],T)
  
  def prepend[B >: T](item: B, tcomb: (B,B) => B): Part[B]
  def append[B >: T](item: B, tcomb: (B,B) => B): Part[B]
  
}

object Part {
  
  def apply[B](): Part[B] = EmptyNode
  
  def apply[B](v: B): Part[B] = LeafNode(v)
  
  def apply[B](left: Part[B], v: B): Part[B] = left | Part(v)
  
  def apply[B](left: Part[B], right: Part[B]): Part[B] = {
    
    (left,right) match {
      case (EmptyNode, _) => right
      case (_,EmptyNode)  => left
      case (l: PartImpl[B], r: PartImpl[B]) => 
        SplitNode(l,r)
      case _  =>
        throw exceptionWrongApplyPartitionTypes(left, right)
    }
  }
  
  /**
   * Partial implementation of Part holds implementation-specific
   * values such as formatting methods
   */
  abstract class PartImpl[T] extends Part[T] {
    
    def isSplit = false
    def isEmpty = false
    def isLeaf = false
    
    
    override def | [B >: T](that: Part[B]): Part[B] = 
      Part(this,that)
      
    override def |+ [B >: T](that: B): Part[B] =
      Part(this,Part(that))
      
    override def combineL[B >: T](tcomb: (B,B) => B): Part[B] =
        throw exceptionCombineNotSupported
  
    override def combineR[B >: T](tcomb: (B,B) => B): Part[B] =
        combineL[B](tcomb)
        
    override def extractL: (T,Part[T]) =
        throw exceptionExtractNotSupported
      
    override def extractR: (Part[T],T) =
        throw exceptionExtractNotSupported

    override def visit(tconsumer: (T,PCTX) => Unit): Unit =
      visit(List[Int](), tconsumer)
    
    // implementation specific
      
    def visit(ctx: PCTX, tconsumer: (T,PCTX) => Unit): Unit
      
    def formatSpec: String = "%s"
    def subFormatSpec: String = formatSpec
 
  }
  
  private case object EmptyNode extends PartImpl[Nothing] {
    
    override def isEmpty = true
    
    override def visit(ctx: PCTX, tconsumer: (Nothing,PCTX) => Unit) 
    {}
    
    override def toString = "(|)"
    
    override def prepend[B](item: B, tcomb: (B,B) => B): Part[B] = 
        Part(item)
        
    override def append[B](item: B, tcomb: (B,B) => B): Part[B] = 
        Part(item)
  }
  
  sealed private case class LeafNode[T](v: T) extends PartImpl[T] {
    
    override def isLeaf = true
    
    override def visit(ctx: PCTX, tconsumer: (T,PCTX) => Unit) 
      = tconsumer(v, ctx);
    
    override def toString = formatSpec.format(v.toString())
    
    override def extractL: (T,Part[T]) = (v, EmptyNode)
    
    override def extractR: (Part[T],T) = (EmptyNode,v)
    
    override def prepend[B >: T](item: B, tcomb: (B,B) => B): Part[B] =
        Part(tcomb(item, v))
    
    override def append[B >: T](item: B, tcomb: (B,B) => B): Part[B] =
        Part(tcomb(v, item))
    
  }
  
  sealed private case class SplitNode[T](left: PartImpl[T], right: PartImpl[T]) 
    extends PartImpl[T] 
  {
    override def isSplit = true
    
    override def visit(ctx:PCTX, tconsumer: (T,PCTX) => Unit) {
      left.visit(0 :: ctx, tconsumer)
      right.visit(1 :: ctx, tconsumer)
    }
    
    override def extractL: (T,Part[T]) = {
      val (value,lr) = left.extractL
      (value, lr | right)
    }
    
    override def extractR: (Part[T],T) = {
      val (rl,value) = right.extractR
      (left | rl, value)
    }
    
    override def prepend[B >: T](item: B, tcomb: (B,B) => B): Part[B] = 
      left.prepend(item, tcomb) | right
    
    override def append[B >: T](item: B, tcomb: (B,B) => B): Part[B] =
      left | right.append(item, tcomb)

    override def combineL[B >: T](tcomb: (B,B) => B): Part[B] = {
      val (leaf,newright) = right.extractL
      val newleft = left.append(leaf, tcomb)
      newleft | newright
    }
    
    override def combineR[B >: T](tcomb: (B,B) => B): Part[B] = {
      val (newleft,leaf) = left.extractR
      val newright = right.prepend(leaf, tcomb)
      newleft | newright
    }

    override def subFormatSpec: String = "(%s)"
    
    override def toString : String = {
      
      val ls : String = left.toString 
      val rs: String = right.toString
      val lformat: String = left.formatSpec
      val rformat: String = right.subFormatSpec
      val sformat = "%s|%s".format(lformat, rformat)
      sformat.format(ls,rs)
      
    }

  }
  
  def exceptionWrongApplyPartitionTypes(left: Part[_], right: Part[_]) 
  : RuntimeException
    = new RuntimeException(
      "Unsupported Partition constructor Part(%s,%s)".format(
        left.getClass, right.getClass))
 
  def exceptionCombineNotSupported : RuntimeException
    = new RuntimeException(
      "combine call not supported on non-partitioned nodes")
  
  def exceptionExtractNotSupported : RuntimeException
    = new RuntimeException(
      "extract call not supported on empty nodes")
  
}