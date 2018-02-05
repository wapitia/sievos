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
package org.sievos.lexmodel.sp1.impl;

import static org.sievos.kern.TI.toTWhen;

import org.sievos.kern.TI;
import org.sievos.lex.SievosParser.Bund1Context;
import org.sievos.lex.SievosParser.BundXContext;
import org.sievos.lex.SievosParser.CompositeContext;
import org.sievos.lex.SievosParser.FuncallContext;
import org.sievos.lex.SievosParser.FuncallExprContext;
import org.sievos.lex.SievosParser.IdentifierContext;
import org.sievos.lex.SievosParser.Part1Context;
import org.sievos.lex.SievosParser.PartXContext;
import org.sievos.lex.SievosParser.TlineContext;
import org.sievos.lex.SievosVisitor;
import org.sievos.lexmodel.sp1.BundLN;
import org.sievos.lexmodel.sp1.CompositeFunctionLN;
import org.sievos.lexmodel.sp1.ExprLN;
import org.sievos.lexmodel.sp1.IdentifierLN;
import org.sievos.lexmodel.sp1.SP1Node;
import org.sievos.lexmodel.sp1.SP1NodeProducer;
import org.sievos.lexmodel.sp1.SingleLN;

import org.antlr.v4.runtime.tree.AbstractParseTreeVisitor;
import org.antlr.v4.runtime.tree.ParseTree;
import org.antlr.v4.runtime.tree.ParseTreeVisitor;

public class SP1AntlrVisitor extends AbstractParseTreeVisitor<SP1Node>
    implements ParseTreeVisitor<SP1Node>, SievosVisitor<SP1Node>
{
    final SP1NodeProducer nodes;

    public SP1AntlrVisitor(final SP1NodeProducer nodeProducer) {
        this.nodes = nodeProducer;
    }

    /*
        @rule expr: ExprLN
                 def funcallExpr(fcall: CompositeFunctionLN)
    */
    @Override
    public ExprLN visitFuncallExpr(final FuncallExprContext ctx) {

        final CompositeFunctionLN fcall = castVisit(ctx.fcall());
//        return makeExpr(fcall);
        return nodes.funcallExpr(fcall);
    }

    /*
        @rule fcall: CompositeFunctionLN
                 def funcall(part: BundLN, fname: IdentifierLN)
                 def composite(fcall: CompositeFunctionLN, fname: IdentifierLN)
    */
    @Override
    public CompositeFunctionLN visitFuncall(final FuncallContext ctx) {

        final BundLN ptp = castVisit(ctx.part());
        final IdentifierLN fnameName = castVisit(ctx.fname());
//        return makeCompositeFunction(fnameName, ptp);
        return nodes.funcall(ptp, fnameName);
    }

    @Override
    public CompositeFunctionLN visitComposite(final CompositeContext ctx) {

        final CompositeFunctionLN subfname = castVisit(ctx.fcall());
        final IdentifierLN fnameName = castVisit(ctx.fname());
//        return makeCompositeFunction(fnameName, subfname);
        return nodes.composite(fnameName, subfname);
    }

    /*
        @rule part: BundLN
                 def part1(bund: BundLN)
                 def partX(part: BundLN, bund: BundLN)
     */
    @Override
    public BundLN visitPart1(final Part1Context ctx) {

        final BundLN tbund = castVisit(ctx.bund());
        return nodes.part1(tbund);
    }

    @Override
    public BundLN visitPartX(final PartXContext ctx)
    {
        final BundLN tpart = castVisit(ctx.part());
        final BundLN tbund = castVisit(ctx.bund());
        return nodes.partX(tpart, tbund);
    }

    /*
        @rule bund: BundLN
                 def bund1(tline: SingleLN)
                 def bundX(tline: SingleLN, bund: BundLN)
    */
    @Override
    public BundLN visitBund1(final Bund1Context ctx)
    {
        final SingleLN singNode = castVisit(ctx.tline());
        return nodes.bund1(singNode);
    }

    @Override
    public BundLN visitBundX(final BundXContext ctx)
    {
        final BundLN bundNode = castVisit(ctx.bund());
        final SingleLN singNode = castVisit(ctx.tline());
        return nodes.bundX(singNode, bundNode);
    }

    /*
        @rule fname: IdentifierLN
                 def identifier(id: String)
    */
    @Override
    public IdentifierLN visitIdentifier(final IdentifierContext ctx)
    {
        final String identString = ctx.IDENT().getText();
        return nodes.identifier(identString);
    }

    /*
        @rule tline: SingleLN
                 def tline(ti: TI)
    */
    @Override
    public SingleLN visitTline(final TlineContext ctx)
    {
        final TI ti = toTWhen(ctx.T() != null);
        return nodes.tline(ti);
    }

    <T extends SP1Node> T
    castVisit(final ParseTree parseTree)
    {
        if (parseTree == null) return null;
        @SuppressWarnings("unchecked")
        final ParseTreeVisitor<? extends T> thisAsTVisitor =
            (ParseTreeVisitor<? extends T>) this;
        final T result = parseTree.accept(thisAsTVisitor);
        return result;
    }

}