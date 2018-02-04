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
package org.sievos.lexmodel.impl.sp1;

import java.util.ArrayList;
import java.util.BitSet;
import java.util.Collections;
import java.util.Iterator;
import java.util.List;
import java.util.Optional;

import org.sievos.kern.TI;
import org.sievos.lexmodel.sp1.CompExprLN;
import org.sievos.lexmodel.sp1.CompositeFunctionLN;
import org.sievos.lexmodel.sp1.ExprLN;
import org.sievos.lexmodel.sp1.IdentifierLN;
import org.sievos.lexmodel.sp1.SP1Node;
import org.sievos.lexmodel.sp1.SP1NodeProducer;
import org.sievos.lexmodel.sp1.TBundLN;
import org.sievos.lexmodel.sp1.TPartLN;
import org.sievos.lexmodel.sp1.TSingleLN;

import com.wapitia.common.collections.OptionalIterable;

/**
 * Collection of concrete {@link SP1Node} extension types, suitable
 * for the SievosLex parsers to compile expressions
 *
 */
public class SP1NodeFactory implements SP1NodeProducer {

	// The SP1NodeProducer API

	@Override
	public ExprLN funcallExpr(final CompositeFunctionLN fcall) {
		return makeExpr(fcall);
	}

	@Override
	public CompositeFunctionLN funcall(final TBundLN ptp,
		final IdentifierLN fnameName)
	{
		// TODO: Smarter than cast?
		return makeCompositeFunction(fnameName, (TBundImpl) ptp);
	}

	@Override
	public CompositeFunctionLN composite(final IdentifierLN fnameName,
		final CompositeFunctionLN subfname)
	{
		// TODO: Smarter than cast?
		return makeCompositeFunction(fnameName, (CompositeFunctionImpl) subfname);
	}

	@Override
	public TBundLN part1(final TBundLN tbund) {
		return tbund;
	}

	@Override
	public TBundLN partX(final TBundLN tpart, final TBundLN tbund) {
		return makePart(tpart, tbund);
	}

	@Override
	public TBundLN bund1(final TSingleLN singNode) {
		return makeTBundle(singNode);
	}

	@Override
	public TBundLN bundX(final TSingleLN singNode, final TBundLN bundNode) {
		// TODO: Smarter than cast?
		return makeTBundle(singNode, (TBundImpl) bundNode);
	}

	@Override
	public IdentifierLN identifier(final String identString) {
		return makeIdentifier(identString);
	}

	@Override
	public TSingleLN tline(final TI ti) {
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

	private static class TSingleImpl extends SP1Base implements TSingleLN {

		private final TI state;

		public TSingleImpl(final TI state) {
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

	private static class TPartImpl extends SP1Base implements TPartLN {
	}

	private static class TBundImpl extends TSingleImpl implements TBundLN, Iterable<TI>
	{

		static final OptionalIterable<TI,TBundImpl> iter =
			new OptionalIterable<TI,TBundImpl>(
				TBundImpl::getState, TBundImpl::getNext);

		private final Optional<TBundImpl> next;

		public TBundImpl(final TI state,
			final Optional<TBundImpl> next)
		{
			super(state);
			this.next = next;
		}


		public Optional<TBundImpl> getNext() {
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
			return 1 + next.map(TBundImpl::size).orElse(0);
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
		public TBundLN pipe(final TBundLN tbund) {
			// TODO Auto-generated method stub
			return null;
		}

	}

	private static class ExprImpl implements ExprLN
	{

	}

	private static class CompExprImpl implements CompExprLN {

		CompositeFunctionLN compfunc;

		public CompExprImpl(final CompositeFunctionLN compfunc) {
			this.compfunc = compfunc;
		}

	}

	private static class CompositeFunctionImpl implements CompositeFunctionLN {

		private final List<String> funcList;
		private final SP1BundAccum tuple;

		CompositeFunctionImpl(final List<String> funcList, final SP1BundAccum tuple) {
			this.funcList = new ArrayList<>(funcList);
			this.tuple = tuple;
		}

		@Override
		public List<String> getFuncList() {
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

	static TSingleLN makeTSingle(final TI ti) {
		return new TSingleImpl(ti);
	}

	static CompExprLN makeExpr(final CompositeFunctionLN compfunc) {
		return new CompExprImpl(compfunc);
	}


	static TBundLN makeTBundle(final TSingleLN singNode)
	{
		return new TBundImpl(singNode.getState(), Optional.empty());
	}

	static TBundLN makePart(final TBundLN tpart, final TBundLN tbund) {
		final TBundLN result = tpart.pipe(tbund);
		return result;
	}


	static TBundLN makeTBundle(final TSingleLN singNode,
		final TBundImpl tupNode)
	{
		return new TBundImpl(singNode.getState(), Optional.of(tupNode));
	}

	static CompositeFunctionLN makeCompositeFunction(
		final IdentifierLN identNode,
		final TBundImpl ptp)
	{
		final String identName = identNode.getIdent();
		final SP1BundAccum tuple = ptp.makeBundAccum();
		final List<String> slist = Collections.<String> singletonList(identName);
		return new CompositeFunctionImpl(slist, tuple);
	}

	static CompositeFunctionLN makeCompositeFunction(
		final IdentifierLN funcName, final CompositeFunctionImpl tupNode) {
		final String identName = funcName.getIdent();
		final List<String> funcList = tupNode.getFuncList();
		final List<String> newList = new ArrayList<>(funcList.size() + 1);
		newList.add(identName);
		newList.addAll(funcList);

		final SP1BundAccum tuple = tupNode.getTuple();
		return new CompositeFunctionImpl(newList, tuple);
	}

	/**
	 * @return
	 */
	public static SP1NodeProducer instance() {
		return instance;
	}

	static SP1NodeProducer instance = new SP1NodeFactory();

	// constructor is private as this is a singleton instance
	protected SP1NodeFactory() {}

}