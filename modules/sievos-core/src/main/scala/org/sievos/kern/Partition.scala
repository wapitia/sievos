/*
 * Copyright 2016-present wapitia.com
 *
 * Redistribution and use in source and binary forms, with or without
 * modification, are permitted provided that the following conditions are
 * met:
 *
 * - Redistribution of source code must retain the above copyright
 *   notice, this list of conditions and the following disclaimer.
 *
 * - Redistribution in binary form must reproduce the above copyright
 *   notice, this list of conditions and the following disclaimer in the
 *   documentation and/or other materials provided with the distribution.
 *
 * Neither the name of wapitia.com or the names of contributors may be used to
 * endorse or promote products derived from this software without specific
 * prior written permission.
 *
 * This software is provided "AS IS," without a warranty of any kind.
 * ALL EXPRESS OR IMPLIED CONDITIONS, REPRESENTATIONS AND WARRANTIES,
 * INCLUDING ANY IMPLIED WARRANTY OF MERCHANTABILITY, FITNESS FOR A
 * PARTICULAR PURPOSE OR NON-INFRINGEMENT, ARE HEREBY EXCLUDED.
 * WAPITIA.COM ("WAPITIA") AND ITS LICENSORS SHALL NOT BE LIABLE FOR
 * ANY DAMAGES SUFFERED BY LICENSEE AS A RESULT OF USING, MODIFYING OR
 * DISTRIBUTING THIS SOFTWARE OR ITS DERIVATIVES. IN NO EVENT WILL WAPITIA OR
 * ITS LICENSORS BE LIABLE FOR ANY LOST REVENUE, PROFIT OR DATA, OR FOR
 * DIRECT, INDIRECT, SPECIAL, CONSEQUENTIAL, INCIDENTAL OR PUNITIVE
 * DAMAGES, HOWEVER CAUSED AND REGARDLESS OF THE THEORY OF LIABILITY,
 * ARISING OUT OF THE USE OF OR INABILITY TO USE THIS SOFTWARE, EVEN IF
 * WAPITIA HAS BEEN ADVISED OF THE POSSIBILITY OF SUCH DAMAGES.
 */
package org.sievos.kern

/**
 * Part is a simple binary tree representation called here a Partition.
 * Each Part is a node in the binary tree. A node may either be a
 * LeafNode (terminal node) or a SplitNode (non-terminal node).
 * <p>
 * A node may also be the "empty" (node EmptyNode), which is a single
 * Part object representing an empty Part tree.
 * An EmptyNode cannot be a node in any other tree.
 * <p>
 * The creation of the tree is strictly enforced by the Part constructor
 * apply methods and by the extract/prepend/combine operations so that:
 *
 * <ul>
 * <li>Each LeafNode holds a single value and no children.
 * <li>Each SplitNode holds two child Parts (left and right) and no values.
 *     Both left and right are either a LeafNode or recursively another
 *     SplitNode. They cannot be null or missing.
 * <li>All nodes are immutable. To change a value, a new tree is constructed
 *     as the old tree is traversed via extract/prepend/combine operations.
 * <li>The natural construction order of the Part tree is preserved.
 * </ul>
 * @param T the types of the leaf values in the tree
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
  
  def filter(filter: T => Boolean): Part[T] =
    filterMap(filter, t => t)
  
  def map[U](map: T => U): Part[U] =
    filterMap(t => true, map)
  
  /**
   * Filter the elements in this partition and map the ones
   * for which filter returns true into objects of type U,
   * and return a new Part with the same structure as this one.
   */
  def filterMap[U](filter: T => Boolean, map: T => U): Part[U]

  /**
   * Visit with no context.
   */
  def visit(tconsumer: T => Unit): Unit
      
  /**
   * Visit each partition which can deliver items of type T to
   * the supplied consumer
   */
  def ctxVisit(tconsumer: (T,PCTX) => Unit): Unit

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
  
  def size: Int
  
  def depth: Int

  /**
   * Combine the left-most two partitions of this tree into a single partition
   * value in accordance with the supplied combining function.
   * This returns the newly adjusted Part tree, having one fewer partitions.
   * <p>
   * The resultant Part tree will be a LeafNode or SplitNode.
   * The new partition value will be the result of tcomb(left,right).
   * <p>
   * combineL may be called only when isSplit is true, when the tree has two
   * partitions. Otherwise a runtime error will result.
   */
  def combineL[B >: T](tcomb: (B,B) => B): Part[B]

  /**
   * Combine the right-most two partitions of this tree into a single partition
   * value in accordance with the supplied combining function.
   * This returns the newly adjusted Part tree, having one fewer partitions.
   * <p>
   * The resultant Part tree will be a LeafNode or SplitNode.
   * The new partition value will be the result of tcomb(left,right).
   * <p>
   * combineR may be called only when isSplit is true, when the tree has two
   * partitions. Otherwise a runtime error will result.
   */
  def combineR[B >: T](tcomb: (B,B) => B): Part[B]

  /**
   * Extract the left-most partition from this Part tree, returning a tuple
   * of the extracted partition and the resultant tree following the extraction.
   * <p>
   * The resultant Part tree may be a LeafNode or SplitNode or the EmptyNode
   * object if this source Part is a LeafNode.
   * <p>
   * extractL may be called only when isSplit is true or isLeaf is true, when
   * the tree has something in it. Otherwise a runtime error will result.
   */
  def extractL: (T,Part[T])

  /**
   * Extract the right-most partition from this Part tree, returning a tuple
   * of the resultant tree following the extraction and the extracted partition.
   * <p>
   * The resultant Part tree may be a LeafNode or SplitNode or the EmptyNode
   * object if this source Part is a LeafNode.
   * <p>
   * extractR may be called only when isSplit is true or isLeaf is true, when
   * the tree has something in it. Otherwise a runtime error will result.
   */
  def extractR: (Part[T],T)

  /**
   * Prepend the supplied item to the partitions left-most leaf node value
   * according to the behavior of the supplied combiner function.
   * A new Part is returned representing the new modified tree.
   * The resultant tree will be a LeafNode or SplitNode.
   */
  def prepend[B >: T](item: B, tcomb: (B,B) => B): Part[B]

  /**
   * Append the supplied item to the partitions right-most leaf node value
   * according to the behavior of the supplied combiner function.
   * A new Part is returned representing the new modified tree.
   * The resultant tree will be a LeafNode or SplitNode.
   */
  def append[B >: T](item: B, tcomb: (B,B) => B): Part[B]
  
  def skewL: Part[T]
  
  def skewR: Part[T]

}

object Part {

  /**
   * empty constructor provides the EmptyNode node object
   */
  def apply[B](): Part[B] = EmptyNode

  /**
   * Single-value constructor gives a single LeafNode, a "tree" of
   * a single item.
   */
  def apply[B](v: B): Part[B] = LeafNode(v)

  /**
   * Constructor takes a partition and then an item to add to the
   * partition on the right.
   * If the Part supplied is EmptyNode, then this results in a LeafNode.
   * Otherwise this results in the SplitNode (left | Part(v))
   * <p>
   * The supplied partition must be one created here (that is, an instance
   * of PartImpl) otherwise a runtime exception shall be thrown.
   */
  def apply[B](left: Part[B], v: B): Part[B] = left | Part(v)

  /**
   * Constructor takes a partition and then an item to add to the
   * partition on the left.
   * If the supplied Part is the EmptyNode then this results in a LeafNode.
   * Otherwise this results in a SplitNode of (Part(v) | right)
   * <p>
   * The supplied partition must be one created here (that is, an instance
   * of PartImpl) otherwise a runtime exception shall be thrown.
   */
  def apply[B](v: B, right: Part[B]): Part[B] = Part(v) | right

  /**
   * Constructor takes two partition resulting in the SplitNode (left | right).
   * <p>
   * If either left or right is the EmptyNode, then this just returns the
   * other, as SplitNode cannot use EmptyNode as one of its child branches.
   * <p>
   * The supplied partitions must be ones created here (that is, instances
   * of PartImpl) otherwise a runtime exception shall be thrown.
   */
  def apply[B](left: Part[B], right: Part[B]): Part[B] = 
    (left,right) match {
      case (EmptyNode, _) => right
      case (_,EmptyNode)  => left
      case (l: PartImpl[B], r: PartImpl[B]) =>
        SplitNode(l,r)
      case _  =>
        throw WrongApplyPartitionTypesException(left, right)
    }

  /**
   * Partial implementation of Part holds implementation-specific
   * values such as formatting methods
   */
  abstract class PartImpl[T] extends Part[T] {

    def isSplit = false
    def isEmpty = false
    def isLeaf = false

    override def | [B >: T](that: Part[B]): Part[B]= Part(this,that)

    override def |+ [B >: T](that: B): Part[B] = Part(this,Part(that))

    override def combineL[B >: T](tcomb: (B,B) => B): Part[B] = 
      throw CombineNotSupportedException()

    override def combineR[B >: T](tcomb: (B,B) => B): Part[B] = 
      combineL[B](tcomb)

    override def extractL: (T,Part[T]) = 
      throw ExtractNotSupportedException()

    override def extractR: (Part[T],T) = 
      throw ExtractNotSupportedException()

    override def visit(tconsumer: T => Unit): Unit
      
    override def ctxVisit(tconsumer: (T,PCTX) => Unit): Unit = 
      ctxVisit(List[Int](), tconsumer)
  
    override def skewL: Part[T] = this
    
    override def skewR: Part[T] = this

    def ctxVisit(ctx: PCTX, tconsumer: (T,PCTX) => Unit): Unit

    def formatSpec = "%s"

    def subFormatSpec = formatSpec
  }

  /**
   * The EmptyNode is a single object shared as all empty nodes.
   */
  private case object EmptyNode extends PartImpl[Nothing] {

    override def isEmpty = true
  
    override def size = 0
    
    override def depth = 0
    
    override def filterMap[U](filter: Nothing => Boolean, map: Nothing => U) =
      EmptyNode
    
    override def visit(tconsumer: Nothing => Unit) {
    }
      
    override def ctxVisit(ctx: PCTX, tconsumer: (Nothing,PCTX) => Unit) {
    }

    override def toString = "()"

    override def prepend[B](item: B, NOT_USED: (B,B) => B): Part[B] = 
      Part(item)

    override def append[B](item: B, NOT_USED: (B,B) => B): Part[B] = 
      Part(item)
  }

  /**
   * LeafNode contains one value and no children.
   */
  sealed private case class LeafNode[T](v: T) extends PartImpl[T] {

    override def isLeaf = true
  
    override def size = 1
    
    override def depth = 1

    override def filterMap[U](filter: T => Boolean, map: T => U) =
      if (filter(v)) Part(map(v))
      else           EmptyNode
      
    override def visit(tconsumer: T => Unit) =
      tconsumer(v)
      
    override def ctxVisit(ctx: PCTX, tconsumer: (T,PCTX) => Unit) = 
      tconsumer(v, ctx)

    override def toString = formatSpec.format(v.toString())

    override def extractL: (T,Part[T]) = (v, EmptyNode)

    override def extractR: (Part[T],T) = (EmptyNode,v)

    override def prepend[B >: T](item: B, tcomb: (B,B) => B): Part[B] = 
      Part(tcomb(item, v))

    override def append[B >: T](item: B, tcomb: (B,B) => B): Part[B] = 
      Part(tcomb(v, item))
  }

  /**
   * SplitNode contains left and right child LeafNodes or SplitNodes,
   * and no values
   */
  sealed private case class SplitNode[T](left: PartImpl[T], right: PartImpl[T])
    extends PartImpl[T]
  {
    override def isSplit = true

    override def size = left.size + right.size
    
    override def depth = 1 + Math.max(left.depth, right.depth)
    
    override def filterMap[U](filter: T => Boolean, map: T => U) = {
      val newleft = left.filterMap(filter, map)
      val newright = right.filterMap(filter, map)
      newleft | newright
    }
      
    override def visit(tconsumer: T => Unit) {
      left.visit(tconsumer)
      right.visit(tconsumer)
    }
      
    override def ctxVisit(ctx:PCTX, tconsumer: (T,PCTX) => Unit) {
      left.ctxVisit(0 :: ctx, tconsumer)
      right.ctxVisit(1 :: ctx, tconsumer)
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

    override def skewL: Part[T] = {
      val llbal = left.skewL
      val rlbal = right.skewL
      (llbal,rlbal) match {
        case (SplitNode(ll,lr), _) => Part(ll,Part(lr,rlbal))
        case (_, SplitNode(rl,rr)) => Part(Part(llbal,rl),rr)
        case _                     => this;
      }      
    }
    
    override def skewR: Part[T] = {
      val lrbal = left.skewR
      val rrbal = right.skewR
      (lrbal,rrbal) match {
        case (_, SplitNode(rl,rr)) => Part(Part(lrbal,rl),rr)
        case (SplitNode(ll,lr), _) => Part(ll,Part(lr,rrbal))
        case _                     => this;
      }      
    }
    
    override def subFormatSpec: String = "(%s)"

    override def toString : String = {
      val sformat = "%s|%s".format(left.formatSpec, right.subFormatSpec)
      sformat.format(left,right)
    }

  }

  abstract class PartitionException(msg: String) extends RuntimeException(msg)
    
  case class WrongApplyPartitionTypesException(left: Part[_], right: Part[_]) 
  extends PartitionException(
    "Unsupported Partition constructor Part(%s,%s)".format(
      left.getClass, right.getClass))

  case class CombineNotSupportedException() extends PartitionException(
      "combine call not supported on non-partitioned nodes")

  case class ExtractNotSupportedException() extends PartitionException(
      "extract call not supported on empty nodes")

}