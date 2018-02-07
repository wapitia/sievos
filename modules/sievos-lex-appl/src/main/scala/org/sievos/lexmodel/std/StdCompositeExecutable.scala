/*
 * Copyright 2016-2018 wapitia.com
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
package org.sievos.lexmodel
package std

import org.sievos.lexmodel.sp1.impl.StdPartImpl
import org.sievos.kern.Part

import scala.collection.JavaConverters._

/**
 * Executable takes a Standard Partition and a list of Partion Functions
 * and applies the composite function chain to each bundle of the given
 * partition.
 */
// TODO: pure Scala
class StdCompositeExecutable(inputPart: StdPartProvider, 
    funcList: java.util.List[StdPartFunction]) 
  extends Executable 
{
  
  val scalaFuncList: scala.collection.mutable.Buffer[StdPartFunction] = 
    funcList.asScala

  override def execute(): StdPartImpl = {

      val part: Part[StdBund] = inputPart.partition
      val resultPart: Part[StdBund] = part.map(bund => composeEachBund(bund))
      new StdPartImpl(resultPart)
  }

  def composeEachBund(bund: StdBund): StdBund = {
      var accumBund: StdBund = bund
      for (pf <- scalaFuncList) 
        accumBund = pf.execute(accumBund)
      accumBund
    }
}