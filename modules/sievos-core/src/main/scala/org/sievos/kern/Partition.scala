package org.sievos.kern

/**
 * Location within a Part binary tree partition
 */
trait PartCtx[+IX] {
  
  def asIndexlist: List[IX]
  def push[B >: IX](v: B): PartCtx[B]
  def + [B >: IX](v: B): PartCtx[B] = push(v)
  
}

object PartCtx {
  def apply[X](): PartCtx[X] = new PCImpl[X](Nil)
  
  sealed class PCImpl[X](list: List[X]) extends PartCtx[X] {
    def asIndexlist = list
    def push[B >: X](v: B) = new PCImpl(v :: list)
  }
  
}

trait Part[+T] {
 
  /**
   * operator '|'
   * Make a BiPartiion between this one and the supplied other partition,
   * forming this | that
   */
  def | [B >: T](that: Part[B]): Part[B] = 
    Part(this,that)
  
  /**
   * operator '|+'
   * Make a BiPartiion between this one and the supplied leaf,
   * forming this | that
   */
  def |+ [B >: T](that: B): Part[B] =
    Part(this,Part(that) )

  /**
   * Visit each partition which can deliver items of type T to
   * the supplied consumer
   */
  def visit(ctx: PartCtx[Int], 
    tconsumer: (T,PartCtx[Int]) => Unit)
  : Unit
  
  def isSplit = false
  
  /**
   * combine is callable only when isSplit is true  
   */
  def combineL[B >: T](tcomb: (B,B) => B): Part[B] =
      throw new RuntimeException

  def combineR[B >: T](tcomb: (B,B) => B): Part[B] =
      combineL[B](tcomb)
  
  def extractL: (T,Part[T])
  def extractR: (Part[T],T)
  def prepend[B >: T](item: B, tcomb: (B,B) => B): Part[B]
  def append[B >: T](item: B, tcomb: (B,B) => B): Part[B]
  
}

object Part {
  
  def apply[T](): Part[T] = EmptyPart
  
  def apply[T](v: T): Part[T] = Leaf[T](v)
  
  def apply[T](left: Part[T], v: T): Part[T] =
    Part(left,Part[T](v))
  
  def apply[T](left: Part[T], right: Part[T]): Part[T] = {
    
    (left,right) match {
      case (EmptyPart, _) => right
      case (_,EmptyPart)  => left
      case _              => BiPart(left,right)
    }
  }
  
  private case object EmptyPart extends Part[Nothing] {
    
    override def visit(ctx: PartCtx[Int], 
      tconsumer: (Nothing,PartCtx[Int]) => Unit) 
    {}
    
    override def toString = "(|)"
    
    override def extractL: (Nothing,Part[Nothing]) =
      throw new RuntimeException
      
    override def extractR: (Part[Nothing],Nothing) =
      throw new RuntimeException
    
    override def prepend[B](item: B, tcomb: (B,B) => B): Part[B] = 
        Part(item)
        
    override def append[B](item: B, tcomb: (B,B) => B): Part[B] = 
        Part(item)
        
  }
  
  sealed private case class Leaf[T](v: T) extends Part[T] {
    
    override def visit(ctx: PartCtx[Int], tconsumer: (T,PartCtx[Int]) => Unit) 
      = tconsumer(v, ctx);
    
    override def toString = "%s".format(v.toString())
    
    override def extractL: (T,Part[T]) = (v, EmptyPart)
    
    override def extractR: (Part[T],T) = (EmptyPart,v)
    
    override def prepend[B >: T](item: B, tcomb: (B,B) => B): Part[B] =
        Part(tcomb(item, v))
    
    override def append[B >: T](item: B, tcomb: (B,B) => B): Part[B] =
        Part(tcomb(v, item))
    
  }
  
  sealed private case class BiPart[T](left: Part[T], right: Part[T]) 
    extends Part[T] 
  {
    
    // It is illegal for BiPart's left or right to be EmptyPart 
    if (left == EmptyPart) throw new RuntimeException
    if (right == EmptyPart) throw new RuntimeException
    
    override def visit(ctx: PartCtx[Int], 
      tconsumer: (T,PartCtx[Int]) => Unit) 
    {
      left.visit(ctx + 0, tconsumer)
      right.visit(ctx + 1, tconsumer)
    }
    
    override def extractL: (T,Part[T]) = {
      val (value,lr) = left.extractL
      left match {
        case _: Leaf[T]   => (value,right)        
        case _: BiPart[T] => (value, lr | right) 
      }
    }
    
    override def extractR: (Part[T],T) = {
      val (rl,value) = right.extractR
      right match {
        case _: Leaf[T]   => (left,value)        
        case _: BiPart[T] => (left | rl, value) 
      }
    }

    
    def biSubFormat(subpart: Part[T]) = subpart match {
          case _ : BiPart[T] => "(%s)"
          case _ => "%s"
    }
    
    override def toString : String = {
      
      val ls : String = left.toString 
      val rs: String = right.toString
      
      val lformat: String = biSubFormat(left)
      val rformat: String = biSubFormat(right)
      val sformat = "%s|%s".format(lformat,rformat)
      sformat.format(ls,rs)
      
    }

    override def prepend[B >: T](item: B, tcomb: (B,B) => B): Part[B] = 
      left.prepend(item, tcomb) | right
    
    override def append[B >: T](item: B, tcomb: (B,B) => B): Part[B] =
      left | right.append(item, tcomb)
   
    override def isSplit = true

    override def combineL[B >: T](tcomb: (B,B) => B): Part[B] = {
      val (leaf,newright) = right.extractL
      val newleft = left.append[B](leaf, tcomb)
      newleft | newright
    }
    
    override def combineR[B >: T](tcomb: (B,B) => B): Part[B] = {
      val (newleft,leaf) = left.extractR
      val newright = right.prepend[B](leaf, tcomb)
      newleft | newright
    }

  }
  
}