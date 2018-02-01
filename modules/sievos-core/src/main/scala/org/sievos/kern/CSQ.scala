package org.sievos.kern

import Kern.{N,TF,F,T}

import CSQ.num;

case class CSQ(c:TF,s:TF,q:TF) {

  override def toString = "%d%d;%d".format(num(c),num(s),num(q));
      
}

object CSQ {
  def csq(n: N): CSQ = CSQ(c = n.b,s = n.x,q = n.y)
  def num(n: TF) = n match {
    case F => 0
    case T => 1
  }
}