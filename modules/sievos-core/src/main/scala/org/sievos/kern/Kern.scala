package org.sievos.kern

import TI.{F,T}

object Kern {
  
  case class N(b:TI,y:TI,x:TI)

  def z(n: N) = n match {
    case N(F,_,_) => n
    case N(T,b,c) => N(T,c,b)
  }
  
  def r(n: N) = n match {
    case N(b,y,x) => N(x,b,y)
  }
  
}
