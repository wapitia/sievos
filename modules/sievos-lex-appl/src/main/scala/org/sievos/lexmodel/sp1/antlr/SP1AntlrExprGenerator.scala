package org.sievos.lexmodel.sp1.antlr

import com.wapitia.lex.StdGenerator

import org.sievos.lexmodel.sp1.SP1Node
import org.sievos.lex.SievosVisitor
import org.sievos.lexmodel.Executable
import org.sievos.lexmodel.sp1.SP1NodeProducer
import org.antlr.v4.runtime.ANTLRErrorListener
import org.antlr.v4.runtime.CodePointCharStream
import org.antlr.v4.runtime.CommonTokenStream
import org.antlr.v4.runtime.Parser

import com.wapitia.lex.antlr.AntlrGeneratorBase
import org.sievos.lex.SievosLexer
import org.sievos.lex.SievosParser
import com.wapitia.lex.StdGenerator

/**
 * Returns an Antlr Generator for the Sievos "expr" goal
 * returning an executable
 * ' 
 * see :modules:sievos-lex-lang:src/main/antlr/org.sievos.lex.Sievos.g4
 */
class SP1AntlrExprGenerator(antlrVisitor: SievosVisitor[SP1Node], 
  eListen: ANTLRErrorListener) 
  extends AntlrGeneratorBase[SP1Node,Executable,SievosParser](antlrVisitor, 
    p => p.expr, n => n.asInstanceOf[Executable], eListen, 
    (cps: CodePointCharStream) => new SievosLexer(cps),
    (cts: CommonTokenStream) => new SievosParser(cts) )

object SP1AntlrExprGenerator {
  
  def apply(nodes: SP1NodeProducer): StdGenerator[Executable] = {
    val visitor: SP1AntlrVisitor2  = new SP1AntlrVisitor2(nodes)
    val eListen: ANTLRErrorListener = new SievosAntlrErrorListener()
    val result: StdGenerator[Executable] = new SP1AntlrExprGenerator(
        visitor, eListen)
    result
  }
}