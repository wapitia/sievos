package org.sievos.kern

import Kern._
import Unicode._
import Logical._

object Visual {
  
 
  def toString(n: N) = "%c%c%c%c%c".format(
    L_TC_BRACKET,
    sym(n.b), sym(n.y), sym(n.x), 
    R_TC_BRACKET);
  
  def sym(o : TF) = o match {
    case F => WHITE_BULLET
    case T => BLACK_CIRCLE
  }

//  def leadsym(o : TF, legal: Boolean) = (o,legal) match {
//    case (_,true) => BULLET
//    case (_,false) => sym(o)
//  }
  
}