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
package com.wapitia.lex.antlr

import org.antlr.v4.runtime.{Lexer, Parser, CharStreams, CodePointCharStream, 
    CommonTokenStream, ANTLRErrorListener}
import org.antlr.v4.runtime.tree.{ParseTree, ParseTreeVisitor}

import com.wapitia.lex.{StdGenerator, StdGenerateStatus}

/**
 * Abstraction for the Antlr parsing, visit, and compilation of
 * a Sievos language goal.
 * @param N the node type for the antlr visitor          
 * @param R the resultant expression type, suitable for some sort of 
 *          execution or interpretation.
 *          // TODO Not sure I need to abstract Parser at the class level
 * @param PT parser extension, the actual parser type, so that the goals
 *           of that parse tree has context here.
 * @param antlrVisitor the Antlr visitor set up to visit nodes in a
 *        Antlr ParseTree          
 * @param goalOfParser, specfic goal within the parser which we want
 *        to target to compile, which results in a ParseTree
 * @param finishResult the compilation function which can map from an Antlr
 *        parse tree node of type N into the result type R
 * @param eListen the error listener must not be null              
 * @param lexerCtor can construct a generated Antlr Lexer instance given
 *        a stream of characters (CodePointCharStream)
 * @param parserCtor can construct a generated Antlr Parser instance given
 *        a stream of tokens (CommonTokenStream)              
 */
class AntlrGeneratorBase[N,R,PT <: Parser](
  antlrVisitor: ParseTreeVisitor[N],
  goalOfParser: PT => ParseTree, 
  finishResult: N => R, 
  eListen: ANTLRErrorListener,
  lexerCtor: CodePointCharStream => Lexer,
  parserCtor: CommonTokenStream => PT) 
  extends StdGenerator[R] 
{
  /**
   * Compile the Sievos expression and return its result
   */
  override def generate(expression: String): (Option[R], StdGenerateStatus) = {
   
    // one-shot use for these things, i guess
    try {
      val parser: PT = AntlrGeneratorBase.createParser(expression, eListen, 
          lexerCtor, parserCtor)
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
  def walkAndFinish(parser: PT): R = 
    walkGoalAndFinish(goalOfParser(parser))

  def walkGoalAndFinish(parseTree: ParseTree): R = 
    finishResult(walkGoal(parseTree))

  def walkGoal(parseTree: ParseTree): N = 
    antlrVisitor.visit(parseTree)
}

object AntlrGeneratorBase {

  def createParser[PT <: Parser](expression: String, eListen: ANTLRErrorListener,
  lexerCtor: CodePointCharStream => Lexer,
  parserCtor: CommonTokenStream => PT): PT =
    createParser(createLexer(expression, eListen, lexerCtor), eListen, 
        lexerCtor, parserCtor)
  
  def createParser[PT <: Parser](lexer: Lexer, eListen: ANTLRErrorListener,
  lexerCtor: CodePointCharStream => Lexer,
  parserCtor: CommonTokenStream => PT): PT = 
    createParser(new CommonTokenStream(lexer), eListen, lexerCtor, parserCtor)
  
  def createLexer(expression: String, eListen: ANTLRErrorListener,
  lexerCtor: CodePointCharStream => Lexer): Lexer = 
    createLexer(CharStreams.fromString(expression), eListen, lexerCtor)
  
  def createParser[PT <: Parser](cts: CommonTokenStream, eListen: ANTLRErrorListener,
  lexerCtor: CodePointCharStream => Lexer,
  parserCtor: CommonTokenStream => PT)
  : PT = 
  {
    val parser = parserCtor(cts)
    parser.removeErrorListeners()
    parser.addErrorListener(eListen)
    parser
  }

  def createLexer(cpcs: CodePointCharStream, eListen: ANTLRErrorListener,
  lexerCtor: CodePointCharStream => Lexer)
  : Lexer = 
  { 
    val lexer = lexerCtor(cpcs)
    lexer.removeErrorListeners()
    lexer.addErrorListener(eListen)
    lexer
  }
  
}
