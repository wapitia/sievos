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
import java.util.BitSet;
import java.util.Collections;
import java.util.Iterator;
import java.util.Optional;

import org.sievos.kern.TI;
import org.sievos.lex.SievosParser;
import org.sievos.lexmodel.Executable;
import org.sievos.lexmodel.sp1.BundLN;
import org.sievos.lexmodel.sp1.CompositeFunctionLN;
import org.sievos.lexmodel.sp1.ExprLN;
import org.sievos.lexmodel.sp1.IdentifierLN;
import org.sievos.lexmodel.sp1.SP1Node;
import org.sievos.lexmodel.sp1.SP1NodeProducer;
import org.sievos.lexmodel.sp1.SingleLN;
import org.sievos.lexmodel.sp1.impl.StdPartImpl;
import org.sievos.lexmodel.std.StdCompositeExecutable;
import org.sievos.lexmodel.std.StdGenerator;
import org.sievos.lexmodel.std.StdPartFunction;
import org.sievos.lexmodel.std.StdPartProvider;

import com.wapitia.common.collections.OptionalIterable;

/**
 * Collection of concrete {@link SP1Node} extension types, suitable
 * for the SievosLex parsers to compile expressions
 *
 */
public class SP1AntlrUtil implements SP1NodeProducer {

    static SP1FuncDict funcDict = new SP1FuncDict();

    // The SP1NodeProducer API

    public StdGenerator<Executable> makeExprCompiler() {

        final SP1AntlrVisitor comp = new SP1AntlrVisitor(this);
        // ExprLN is both the result from the parse tree visit as well
        // as the result executable, making these supplied functions easy
        return new SP1AntlrGenerator<ExprLN,Executable>(comp,
            SievosParser::expr,
            (final SP1Node n) -> (ExprLN) n,
            (final ExprLN n) -> n);

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
        return makeCompositeFunction(fnameName, (BundImpl) ptp);
    }

    @Override
    public CompositeFunctionLN composite(final IdentifierLN fnameName,
        final CompositeFunctionLN subfname)
    {
        // TODO: Smarter than cast?
        return makeCompositeFunction(fnameName, (CompositeFunctionImpl) subfname);
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
        return makeTBundle(singNode, (BundImpl) bundNode);
    }

    @Override
    public IdentifierLN identifier(final String identString) {
        return makeIdentifier(identString);
    }

    @Override
    public SingleLN tline(final TI ti) {
        return makeTSingle(ti);
    }

    static class SP1Base implements SP1Node {

    }

    private static class IdentifierImpl extends SP1Base implements IdentifierLN {

        private final String ident;

        IdentifierImpl(final String ident) {
            this.ident = ident;
        }

        @Override
        public String getIdent() {
            return ident;
        }
        @Override
        public String toString() {
            return ident;
        }
    }

    private static class SingleImpl extends SP1Base implements SingleLN {

        private final TI state;

        public SingleImpl(final TI state) {
            this.state = state;
        }

        @Override
        public TI getState() {
            return state;
        }

        @Override
        public String toString() {
            return SP1BundAccum.tDispChar(state);
        }

    }

    private static class BundImpl extends SingleImpl implements BundLN, Iterable<TI>
    {

        static final OptionalIterable<TI,BundImpl> iter =
            new OptionalIterable<TI,BundImpl>(
                BundImpl::getState, BundImpl::getNext);

        private final Optional<BundImpl> next;

        public BundImpl(final TI state,
            final Optional<BundImpl> next)
        {
            super(state);
            this.next = next;
        }


        public Optional<BundImpl> getNext() {
            return next;
        }

        @Override
        public Iterator<TI> iterator() {
            return iter.iterator(this);
        }

        public SP1BundAccum makeBundAccum() {
            final BitSet bits = asBits();
            return new SP1BundAccum(size(), bits);
        }

        /**
         * An ordered set of bits, as a BitSet.
         * The {@link #getState() state } of this {@code TupNode}
         * is bit 0, the next tuple's state is
         *
         * @return a BitSet of this
         */
        public BitSet asBits() {
            final BitSet bits = new BitSet();
            setbitR(bits, size()-1);
            return bits;
        }

        public int size() {
            return 1 + next.map(BundImpl::size).orElse(0);
        }

        void setbitR(final BitSet bitset, final int ix) {

            if (getState() == TI.T) {
                bitset.set(ix);
            }
            next.ifPresent(nn -> nn.setbitR(bitset, ix-1));
        }

        @Override
        public String toString() {
            return SP1BundAccum.bundleToString(asBits(), size());
        }

        @Override
        public BundLN pipe(final BundLN tbund) {
            // TODO Auto-generated method stub
            return null;
        }

    }

    private static abstract class ExprImpl implements ExprLN
    {
    }


    private static class CompositeFunctionImpl extends ExprImpl
    implements CompositeFunctionLN
    {

        private final java.util.List<StdPartFunction> funcList;
        private final SP1BundAccum tuple;

        CompositeFunctionImpl(final java.util.List<StdPartFunction> funcList, final SP1BundAccum tuple) {
            this.funcList = new ArrayList<>(funcList);
            this.tuple = tuple;
        }

        @Override
        public StdPartProvider execute() {
            final StdCompositeExecutable exec = new StdCompositeExecutable(asPart(), funcList);
            final StdPartImpl result = exec.execute();
            return result;
        }


        @Override
        public StdPartProvider asPart() {
            return tuple.asPartition();
        }

        @Override
        public java.util.List<StdPartFunction> getFuncList() {
            return funcList;
        }

        public SP1BundAccum getTuple() {
            return tuple;
        }

        @Override
        public String toString() {

            final StringBuilder bldr = new StringBuilder();
            bldr.append(tuple.toString());
            bldr.append(' ');
            if (funcList.size() == 1) {
                bldr.append(funcList.get(0).toString());
            }
            else {
                bldr.append(funcList.toString());
            }
            return bldr.toString();
        }


    }

    static IdentifierImpl makeIdentifier(final String ident) {
        return new IdentifierImpl(ident);
    }

    static SingleLN makeTSingle(final TI ti) {
        return new SingleImpl(ti);
    }

    static ExprLN makeExpr(final CompositeFunctionLN compfunc) {
        return compfunc;
    }


    static BundLN makeTBundle(final SingleLN singNode)
    {
        return new BundImpl(singNode.getState(), Optional.empty());
    }

    static BundLN makePart(final BundLN tpart, final BundLN tbund) {
        final BundLN result = tpart.pipe(tbund);
        return result;
    }


    static BundLN makeTBundle(final SingleLN singNode,
        final BundImpl tupNode)
    {
        return new BundImpl(singNode.getState(), Optional.of(tupNode));
    }

    static CompositeFunctionLN makeCompositeFunction(
        final IdentifierLN identNode,
        final BundImpl ptp)
    {
        final String identName = identNode.getIdent();
        final SP1BundAccum tuple = ptp.makeBundAccum();
        final StdPartFunction func = funcDict.getPartFunction(identName);
        final java.util.List<StdPartFunction> slist = Collections
                .<StdPartFunction> singletonList(func);
        return new CompositeFunctionImpl(slist, tuple);
    }

    static CompositeFunctionLN makeCompositeFunction(
        final IdentifierLN funcName, final CompositeFunctionImpl tupNode) {
        final String identName = funcName.getIdent();
        final StdPartFunction partFunction =
                funcDict.getPartFunction(identName);
        final java.util.List<StdPartFunction> funcList = tupNode.getFuncList();
        final java.util.List<StdPartFunction> newList =
                new ArrayList<>(funcList.size() + 1);
        newList.addAll(funcList);
        newList.add(partFunction);

        final SP1BundAccum tuple = tupNode.getTuple();
        return new CompositeFunctionImpl(newList, tuple);
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