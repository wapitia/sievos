package org.sievos.kern

import TI.{F,T}
import Kern.{N}

import CSQ.num;

case class CSQ(c:TI,s:TI,q:TI) {

  override def toString = "%d%d;%d".format(num(c),num(s),num(q));
      
}

object CSQ {
  def csq(n: N): CSQ = CSQ(c = n.b,s = n.x,q = n.y)
  def num(n: TI) = n match {
    case F => 0
    case T => 1
  }
}