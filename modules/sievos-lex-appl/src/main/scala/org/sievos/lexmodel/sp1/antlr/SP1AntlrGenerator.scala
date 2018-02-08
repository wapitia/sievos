/*
 * Copyright 2016-present wapitia.com
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
package sp1
package antlr

import org.antlr.v4.runtime.{CharStreams,CodePointCharStream,CommonTokenStream}
import org.antlr.v4.runtime.tree.ParseTree
import org.sievos.lex.{SievosLexer,SievosParser,SievosVisitor}
import org.sievos.lexmodel.sp1.SP1Node
import org.sievos.lexmodel.std.StdGenerator

/**
 * Abstraction for the Antlr parsing, visit, and compilation of
 * a Sievos language goal.
 * @param R the resultant expression type, suitable for some sort of 
 *          execution or interpretation.
 */
class SP1AntlrGenerator[R](
  antlrVisitor: SievosVisitor[SP1Node],
  goalOfParser: SievosParser => ParseTree, 
  finishResult: SP1Node => R ) 
  extends StdGenerator[R] 
{
  /**
   * Compile the Sievos expression and return its result
   */
  override def compile(expression: String): R = {
   
    // one-shot use for these things, i guess
    val lexer: SievosLexer = SP1AntlrGenerator.createLexer(expression)
    val parser: SievosParser = SP1AntlrGenerator.createParser(lexer)
    val result: R = walkAndFinish(parser)
    result
  }

  /**
   * Visit the parsed sievos expression and return its result
   */
  def walkAndFinish(parser: SievosParser): R = 
    walkGoalAndFinish(goalOfParser(parser))

  def walkGoalAndFinish(parseTree: ParseTree): R = 
    finishResult(antlrVisitor.visit(parseTree))
}

object SP1AntlrGenerator {

  def  createParser(lexer: SievosLexer): SievosParser =  {
    val tokens = new CommonTokenStream(lexer)
    val parser = new SievosParser(tokens)
    parser
  }

  def createLexer(expression: String): SievosLexer = {
    val cpcinput: CodePointCharStream  = CharStreams.fromString(expression)
    val lexer: SievosLexer = new SievosLexer(cpcinput)
    lexer
  }
  
}
