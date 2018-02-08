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
import org.antlr.v4.runtime.ANTLRErrorListener
import org.sievos.lexmodel.std.StdGenerateStatus



/**
 * Abstraction for the Antlr parsing, visit, and compilation of
 * a Sievos language goal.
 * @param R the resultant expression type, suitable for some sort of 
 *          execution or interpretation.
 */
class SP1AntlrGenerator[R](
  antlrVisitor: SievosVisitor[SP1Node],
  goalOfParser: SievosParser => ParseTree, 
  finishResult: SP1Node => R, 
  eListen: ANTLRErrorListener) 
  extends StdGenerator[R] 
{
  /**
   * Compile the Sievos expression and return its result
   */
  override def generate(expression: String): (Option[R], StdGenerateStatus) = {
   
    // one-shot use for these things, i guess
    val parser: SievosParser = SP1AntlrGenerator.createParser(expression,eListen)
    try {
      val result: R = walkAndFinish(parser)
      (Some(result), StdGenerateStatus.OK)
    }
    catch { case x: RuntimeException =>
      // TODO: Handle various types of exceptions, generate custom statuses
      (None, StdGenerateStatus(x))
    }
  }

  /**
   * Visit the parsed sievos expression and return its result
   */
  def walkAndFinish(parser: SievosParser): R = 
    walkGoalAndFinish(goalOfParser(parser))

  def walkGoalAndFinish(parseTree: ParseTree): R = 
    finishResult(walkGoal(parseTree))

  def walkGoal(parseTree: ParseTree): SP1Node = 
    antlrVisitor.visit(parseTree)
    
}

/**
 * Returns an Antlr Generator for the Sievos "expr" goal
 * returning an executable
 * ' 
 * see :modules:sievos-lex-lang:src/main/antlr/org.sievos.lex.Sievos.g4
 */
class AntlrExprGenerator(antlrVisitor: SievosVisitor[SP1Node], 
  eListen: ANTLRErrorListener) 
  extends SP1AntlrGenerator[Executable](antlrVisitor, 
    p => p.expr, n => n.asInstanceOf[Executable], eListen)
  

object SP1AntlrGenerator {

  def createParser(expression: String, eListen: ANTLRErrorListener): SievosParser =
    createParser(createLexer(expression, eListen), eListen)
  
  def createParser(lexer: SievosLexer, eListen: ANTLRErrorListener): SievosParser = 
    createParser(new CommonTokenStream(lexer), eListen)
  
  def createLexer(expression: String, eListen: ANTLRErrorListener): SievosLexer = 
    createLexer(CharStreams.fromString(expression), eListen)
  
  def createParser(cts: CommonTokenStream, eListen: ANTLRErrorListener)
  : SievosParser = 
  {
    val parser = new SievosParser(cts)
    parser.removeErrorListeners()
    parser.addErrorListener(eListen)
    parser
  }

  def createLexer(cpcs: CodePointCharStream, eListen: ANTLRErrorListener)
  : SievosLexer = 
  { 
    val lexer = new SievosLexer(cpcs)
    lexer.removeErrorListeners()
    lexer.addErrorListener(eListen)
    lexer
  }
  
}
