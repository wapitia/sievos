/**
 *
 */
package org.sievos.lexmodel.impl;

import java.util.ArrayList;
import java.util.BitSet;
import java.util.Collections;
import java.util.Iterator;
import java.util.List;
import java.util.Optional;

import com.wapitia.common.collections.OptionalIterable;

/**
 * Collection of concrete {@link LexNode} extension types, suitable
 * for the SievosLex parsers to compile expressions
 *
 */
public interface LexNodeMenagerie {

	static class LexNodeBase implements LexNode {

	}

	static class IdentifierLN extends LexNodeBase {

		private final String ident;

		IdentifierLN(final String ident) {
			this.ident = ident;
		}
		String getIdent() {
			return ident;
		}
		@Override
		public String toString() {
			return ident;
		}
	}

	static class TSingleLN extends LexNodeBase {

		private final boolean state;

		public TSingleLN(final boolean state) {
			this.state = state;
		}

		public boolean getState() {
			return state;
		}

		@Override
		public String toString() {
			final String result = "" + TBundImpl.tDispChar(state);
			return result;
		}

	}

	static class TBundLN extends TSingleLN implements Iterable<Boolean>
	{

		static final OptionalIterable<Boolean,TBundLN> iter =
			new OptionalIterable<Boolean,TBundLN>(
				TBundLN::getState, TBundLN::getNext);

		private final Optional<TBundLN> next;

		public TBundLN(final boolean state,
			final Optional<TBundLN> next)
		{
			super(state);
			this.next = next;
		}


		public Optional<TBundLN> getNext() {
			return next;
		}

		@Override
		public Iterator<Boolean> iterator() {
			return iter.iterator(this);
		}

		public TBundImpl getTuple() {
			final BitSet bits = asBits();
			return new TBundImpl(size(), bits);
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
			return 1 + next.map(TBundLN::size).orElse(0);
		}

		void setbitR(final BitSet bitset, final int ix) {

			if (getState()) {
				bitset.set(ix);
			}
			next.ifPresent(nn -> nn.setbitR(bitset, ix-1));
		}

		@Override
		public String toString() {
			return TBundImpl.bundleToString(asBits(), size());
		}

	}

	static class CompositeFunctionLN extends LexNodeBase {

		private final List<String> funcList;
		private final TBundImpl tuple;

		CompositeFunctionLN(final List<String> funcList, final TBundImpl tuple) {
			this.funcList = new ArrayList<>(funcList);
			this.tuple = tuple;
		}
		List<String> getFuncList() {
			return funcList;
		}
		TBundImpl getTuple() {
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

	static IdentifierLN makeIdentifier(final String ident) {
		return new IdentifierLN(ident);
	}

	static TSingleLN makeTSingle(final boolean trueState) {
		return new TSingleLN(trueState);
	}

	static TBundLN makeTBundle(final TSingleLN singNode)
	{
		return new TBundLN(singNode.getState(), Optional.empty());
	}

	static TBundLN makeTBundle(final TSingleLN singNode,
		final TBundLN tupNode)
	{
		return new TBundLN(singNode.getState(), Optional.of(tupNode));
	}

	static CompositeFunctionLN makeCompositeFunction(
		final IdentifierLN identNode,
		final TBundLN ptp)
	{
		final String identName = identNode.getIdent();
		final TBundImpl tuple = ptp.getTuple();
		final List<String> slist = Collections.<String> singletonList(identName);
		return new CompositeFunctionLN(slist, tuple);
	}

	static CompositeFunctionLN makeCompositeFunction(
		final IdentifierLN funcName, final CompositeFunctionLN tupNode) {
		final String identName = funcName.getIdent();
		final List<String> funcList = tupNode.getFuncList();
		final List<String> newList = new ArrayList<>(funcList.size() + 1);
		newList.add(identName);
		newList.addAll(funcList);

		final TBundImpl tuple = tupNode.getTuple();
		return new CompositeFunctionLN(newList, tuple);
	}

}