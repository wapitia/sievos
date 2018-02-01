package org.sievos.kern

import Kern.{N,F,T}

object Logical {
  
  def F0 = N(F,F,F)
  def F1 = N(F,F,T)
  def N0 = N(T,F,F)
  def N1 = N(T,F,T)
  def N2 = N(F,T,F)
  def N3 = N(F,T,T)
  def F2 = N(T,T,F)
  def F3 = N(T,T,T)
  
  def isLegal(n: N) = n match {
    case N(T,F,_) => true
    case N(F,T,_) => true
    case N(F,F,_) => false
    case N(T,T,_) => false
  }
  
}