

package org.sievos.kern

object Kern {
  
  sealed trait TF
  case object F extends TF
  case object T extends TF
  
  case class N(b:TF,y:TF,x:TF)

  def z(n: N) = n match {
    case N(F,_,_) => n
    case N(T,b,c) => N(T,c,b)
  }
  
  def r(n: N) = n match {
    case N(b,y,x) => N(x,b,y)
  }
  
//  def rot(n: N) = rotr(n)
//  
//  def rotl(n: N) = n match {
//    case N(b,y,x) => N(y,x,b)
//  }
//  
//  def rotr(n: N) = n match {
//    case N(b,y,x) => N(x,b,y)
//  }
//  
}
