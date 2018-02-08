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

import org.antlr.v4.runtime.ParserRuleContext
import org.antlr.v4.runtime.tree.AbstractParseTreeVisitor
import org.antlr.v4.runtime.tree.ParseTree
import org.antlr.v4.runtime.tree.ParseTreeVisitor
import org.sievos.kern.TI
import org.sievos.lex.SievosParser.Bund1Context
import org.sievos.lex.SievosParser.BundXContext
import org.sievos.lex.SievosParser.CompositeContext
import org.sievos.lex.SievosParser.FuncallContext
import org.sievos.lex.SievosParser.FuncallExprContext
import org.sievos.lex.SievosParser.IdentifierContext
import org.sievos.lex.SievosParser.Part1Context
import org.sievos.lex.SievosParser.PartXContext
import org.sievos.lex.SievosParser.TlineContext
import org.sievos.lex.SievosVisitor
import org.antlr.v4.runtime.Token
import com.wapitia.lex.antlr.AntlrVisitorBase
import org.sievos.lexmodel.std.ExprLN
import org.sievos.lexmodel.std.CompositeFunctionLN
import org.sievos.lexmodel.std.BundLN
import org.sievos.lexmodel.std.IdentifierLN
import org.sievos.lexmodel.std.SingleLN
import org.sievos.lexmodel.std.StdLexNodeProducer

class SP1AntlrVisitor(nodes: StdLexNodeProducer) 
extends AntlrVisitorBase[SievosLexNode] with SievosVisitor[SievosLexNode]
{
    /*
       rule expr: ExprLN
              def funcallExpr(fcall: CompositeFunctionLN)
    */
    override def visitFuncallExpr(ctx: FuncallExprContext): ExprLN = {
      validateContext(ctx)
      val fcall = visitTo[CompositeFunctionLN](ctx.fcall())
      val exprNode = nodes.funcallExpr(fcall)
      exprNode
    }

    /*
       rule fcall: CompositeFunctionLN
              def funcall(part: BundLN, fname: IdentifierLN)
              def composite(fcall: CompositeFunctionLN, fname: IdentifierLN)
    */
    override def visitFuncall(ctx: FuncallContext): CompositeFunctionLN = {
      validateContext(ctx)
      val bundNode = visitTo[BundLN](ctx.part())
      val identNode = visitTo[IdentifierLN](ctx.fname())
      nodes.funcall(bundNode, identNode)
    }
    
    override def visitComposite(ctx: CompositeContext): CompositeFunctionLN = {
      validateContext(ctx)
      val compositeNode = visitTo[CompositeFunctionLN](ctx.fcall())
      val identode = visitTo[IdentifierLN](ctx.fname())
      nodes.composite(identode, compositeNode)
    }

    /*
       rule part: BundLN
              def part1(bund: BundLN)
              def partX(part: BundLN, bund: BundLN)
     */
    override def visitPart1(ctx: Part1Context): BundLN = {
      validateContext(ctx)
      val bundNode = visitTo[BundLN](ctx.bund())
      nodes.part1(bundNode)
    }

    override def visitPartX(ctx: PartXContext): BundLN = {
      validateContext(ctx)
       val partNode = visitTo[BundLN](ctx.part())
       val bundNode = visitTo[BundLN](ctx.bund())
       nodes.partX(partNode, bundNode)
    }

    /*
       rule bund: BundLN
              def bund1(tline: SingleLN)
              def bundX(tline: SingleLN, bund: BundLN)
    */
    override def visitBund1(ctx: Bund1Context): BundLN = {
      validateContext(ctx)
      val singNode = visitTo[SingleLN](ctx.tline())
      nodes.bund1(singNode);
    }

    override def visitBundX(ctx: BundXContext): BundLN = {
      validateContext(ctx)
      val bundNode = visitTo[BundLN](ctx.bund())
      val singNode = visitTo[SingleLN](ctx.tline())
      nodes.bundX(singNode, bundNode)
    }

    /*
       rule fname: IdentifierLN
               def identifier(id: String)
    */
    override def visitIdentifier(ctx: IdentifierContext): IdentifierLN = {
      validateContext(ctx)
      val ident: String = ctx.IDENT().getText()
      nodes.identifier(ident)
    }

    /*
       rule tline: SingleLN
              def tline(ti: TI)
    */
    override def visitTline(ctx: TlineContext): SingleLN = {
      validateContext(ctx)
      val ti: TI = TI.toTWhen(ctx.T() != null)
      nodes.tline(ti)
    }
    
}
