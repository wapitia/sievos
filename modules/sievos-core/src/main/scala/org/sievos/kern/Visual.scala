package org.sievos.kern

import TI.{F,T}
import Kern.{N}
import Unicode._
import Logical._

object Visual {
 
  def toString(n: N) = "%c%c%c%c%c".format(
    L_TC_BRACKET,
    sym(n.b), sym(n.y), sym(n.x), 
    R_TC_BRACKET);
  
  def sym(o : TI) = o match {
    case F => WHITE_BULLET
    case T => BLACK_CIRCLE
  }

}