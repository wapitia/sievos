package org.sievos.lexmodel.std

import java.lang.RuntimeException

class StdGenerateStatus(message: String) {
  
  override def toString: String = message
}

object StdGenerateStatus {
  
  def apply(rte: RuntimeException): StdGenerateStatus = 
    new RuntimeExceptionStatus(rte)
  
  case object OK extends StdGenerateStatus("OK")
  
  class RuntimeExceptionStatus(rte: RuntimeException) 
    extends StdGenerateStatus(rte.getMessage)

}
