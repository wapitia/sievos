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
package org.sievos.lexmodel.sp1.antlr;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Optional;

import org.sievos.kern.TI;
import org.sievos.lexmodel.sp1.impl.SP1BundAccum;
import org.sievos.lexmodel.sp1.impl.SP1BundImpl;
import org.sievos.lexmodel.sp1.impl.SP1CompositeFunctionImpl;
import org.sievos.lexmodel.sp1.impl.SP1IdentifierImpl;
import org.sievos.lexmodel.sp1.impl.SP1SingleImpl;
import org.sievos.lexmodel.std.BundLN;
import org.sievos.lexmodel.std.CompositeFunctionLN;
import org.sievos.lexmodel.std.Executable;
import org.sievos.lexmodel.std.ExprLN;
import org.sievos.lexmodel.std.IdentifierLN;
import org.sievos.lexmodel.std.SingleLN;
import org.sievos.lexmodel.std.StdLexNodeProducer;
import org.sievos.lexmodel.std.StdPartFunction;

import com.wapitia.lex.StdGenerator;

/**
 * Collection of concrete {@link SP1Node} extension types, suitable
 * for the SievosLex parsers to compile expressions
 *
 */
// TODO Refactor to Scala
public class SP1AntlrUtil implements StdLexNodeProducer {

    static SP1FuncDict funcDict = new SP1FuncDict();

    // The SP1NodeProducer API

    public StdGenerator<Executable> makeExprGenerator() {

        return SP1AntlrExprGenerator.apply(this);
    }

    @Override
    public ExprLN funcallExpr(final CompositeFunctionLN fcall) {
        return makeExpr(fcall);
    }

    @Override
    public CompositeFunctionLN funcall(final BundLN ptp,
        final IdentifierLN fnameName)
    {
        // TODO: Smarter than cast?
        return makeCompositeFunction(fnameName, (SP1BundImpl) ptp);
    }

    @Override
    public CompositeFunctionLN composite(final IdentifierLN fnameName,
        final CompositeFunctionLN subfname)
    {
        // TODO: Smarter than cast?
        return makeCompositeFunction(fnameName, (SP1CompositeFunctionImpl) subfname);
    }

    @Override
    public BundLN part1(final BundLN tbund) {
        return tbund;
    }

    @Override
    public BundLN partX(final BundLN tpart, final BundLN tbund) {
        return makePart(tpart, tbund);
    }

    @Override
    public BundLN bund1(final SingleLN singNode) {
        return makeTBundle(singNode);
    }

    @Override
    public BundLN bundX(final SingleLN singNode, final BundLN bundNode) {
        // TODO: Smarter than cast?
        return makeTBundle(singNode, (SP1BundImpl) bundNode);
    }

    @Override
    public IdentifierLN identifier(final String identString) {
        return makeIdentifier(identString);
    }

    @Override
    public SingleLN tline(final TI ti) {
        return makeTSingle(ti);
    }

    static SP1IdentifierImpl makeIdentifier(final String ident) {
        return new SP1IdentifierImpl(ident);
    }

    static SingleLN makeTSingle(final TI ti) {
        return new SP1SingleImpl(ti);
    }

    static ExprLN makeExpr(final CompositeFunctionLN compfunc) {
        return compfunc;
    }


    static BundLN makeTBundle(final SingleLN singNode)
    {
        return new SP1BundImpl(singNode.getState(), Optional.empty());
    }

    static BundLN makePart(final BundLN tpart, final BundLN tbund) {
        final BundLN result = tpart.pipe(tbund);
        return result;
    }


    static BundLN makeTBundle(final SingleLN singNode,
        final SP1BundImpl tupNode)
    {
        return new SP1BundImpl(singNode.getState(), Optional.of(tupNode));
    }

    static CompositeFunctionLN makeCompositeFunction(
        final IdentifierLN identNode,
        final SP1BundImpl ptp)
    {
        final String identName = identNode.getIdent();
        final SP1BundAccum tuple = ptp.makeBundAccum();
        final StdPartFunction func = funcDict.getPartFunction(identName);
        final java.util.List<StdPartFunction> slist = Collections
                .<StdPartFunction> singletonList(func);
        return new SP1CompositeFunctionImpl(slist, tuple);
    }

    static CompositeFunctionLN makeCompositeFunction(
        final IdentifierLN funcName, final SP1CompositeFunctionImpl tupNode) {
        final String identName = funcName.getIdent();
        final StdPartFunction partFunction =
                funcDict.getPartFunction(identName);
        final java.util.List<StdPartFunction> funcList = tupNode.getFuncList();
        final java.util.List<StdPartFunction> newList =
                new ArrayList<>(funcList.size() + 1);
        newList.addAll(funcList);
        newList.add(partFunction);

        final SP1BundAccum tuple = tupNode.getTuple();
        return new SP1CompositeFunctionImpl(newList, tuple);
    }

    /**
     * @return
     */
    public static SP1AntlrUtil instance() {
        return instance;
    }

    static SP1AntlrUtil instance = new SP1AntlrUtil();

    // constructor is private as this is a singleton instance
    protected SP1AntlrUtil() {}

}