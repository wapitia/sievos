package org.sievos.lexmodel.impl;

import java.util.ArrayList;
import java.util.BitSet;
import java.util.Collections;
import java.util.Iterator;
import java.util.List;
import java.util.Optional;

import org.antlr.v4.runtime.tree.AbstractParseTreeVisitor;
import org.antlr.v4.runtime.tree.ParseTree;
import org.sievos.lex.SievosParser;
import org.sievos.lex.SievosParser.SingContext;
import org.sievos.lex.SievosParser.TUPContext;
import org.sievos.lex.SievosParser.TXContext;
import org.sievos.lex.SievosVisitor;
import org.sievos.lexmodel.impl.VisitFuncOne.Nodes.CompositeFunc;
import org.sievos.lexmodel.impl.VisitFuncOne.Nodes.IdentNode;
import org.sievos.lexmodel.impl.VisitFuncOne.Nodes.SingNode;
import org.sievos.lexmodel.impl.VisitFuncOne.Nodes.TupNode;

import com.wapitia.common.collections.OptionalIterable;

public class VisitFuncOne extends AbstractParseTreeVisitor<LexNode>
	implements SievosVisitor<LexNode> {


	public static interface Nodes {

		static class LexNodeBase implements LexNode {

		}

		static class IdentNode extends LexNodeBase {

			private final String ident;

			IdentNode(final String ident) {
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

		static class SingNode extends LexNodeBase {
			private final boolean state;

			public SingNode(final boolean state) {
				this.state = state;
			}

			public boolean getState() {
				return state;
			}

			@Override
			public String toString() {
				final String result = "" + NTuple.tDispChar(state);
				return result;
			}

		}

		static class TupNode extends SingNode implements Iterable<Boolean> {

			static final OptionalIterable<Boolean,TupNode> iter =
				new OptionalIterable<Boolean,TupNode>(TupNode::getState, TupNode::getNext);

			private final Optional<TupNode> next;

			public TupNode(final boolean state, final Optional<TupNode> next) {
				super(state);
				this.next = next;
			}


			public Optional<TupNode> getNext() {
				return next;
			}

			@Override
			public Iterator<Boolean> iterator() {

				return iter.iterator(this);
			}

			public NTuple getTuple() {
				final BitSet bits = asBits();
				return new NTuple(size(), bits);
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
				return 1 + next.map(TupNode::size).orElse(0);
			}

			void setbitR(final BitSet bitset, final int ix) {

				if (getState()) {
					bitset.set(ix);
				}
				next.ifPresent(nn -> nn.setbitR(bitset, ix-1));
			}

			@Override
			public String toString() {
				return NTuple.bundleToString(asBits(), size());
			}

		}

		static class CompositeFunc extends LexNodeBase {

			private final List<String> funcList;
			private final NTuple tuple;

			CompositeFunc(final List<String> funcList, final NTuple tuple) {
				this.funcList = new ArrayList<>(funcList);
				this.tuple = tuple;
			}
			List<String> getFuncList() {
				return funcList;
			}
			NTuple getTuple() {
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

	}

	// rule fcall
	// a single FTFTuple or other FTFTuple stream representation which can be
	// applied against a composite functional block
	// (<tuple>) <identifier>...
	//
	// example: 'TFF r z'
	//
//	fcall     : ptp func        # SINGF
//	          | fcall func      # MULTF
//	          ;

	@Override
	public CompositeFunc visitSINGF(final SievosParser.SINGFContext ctx) {

		final TupNode ptp = visitNar(ctx.ptp());
		final IdentNode funcName = visitNar(ctx.func());
		final String identName = funcName.getIdent();
		final NTuple tup = ptp.getTuple();
		final List<String> slist = Collections.<String> singletonList(identName);
		return new CompositeFunc(slist, tup);
	}

	@Override
	public CompositeFunc visitMULTF(final SievosParser.MULTFContext ctx) {

		final CompositeFunc tupNode = visitNar(ctx.fcall());
		final IdentNode funcName = visitNar(ctx.func());
		final String identName = funcName.getIdent();
		final List<String> funcList = tupNode.getFuncList();
		final NTuple tuple = tupNode.getTuple();
		final List<String> newList = new ArrayList<>(funcList.size() + 1);
		newList.add(identName);
		newList.addAll(funcList);
		return new CompositeFunc(newList, tuple);
	}

	@SuppressWarnings("unchecked")
	<T extends LexNode> T
	visitNar(final ParseTree parseTree)
	{
		T result = null;
		if (parseTree != null) {
			result = (T) this.visit(parseTree);
		}
		return result;
	}

	@Override
	public TupNode visitT1(final SievosParser.T1Context ctx) {

		final SingNode singNode = visitNar(ctx.sing());
		final TupNode tup = new TupNode(singNode.getState(), Optional.empty());
		return tup;
	}

	@Override
	public IdentNode visitFunc(final SievosParser.FuncContext ctx) {

		final ParseTree child = ctx.getChild(0);
		final String ident = child.getText();
		final IdentNode IdentNode = new IdentNode(ident);
		return IdentNode;
	}

//	@Override
//	public TupNode visitPTUP(final PTUPContext ctx) {
//
//		final TupNode tupNode = visitNar(ctx.ptp());
//		return tupNode;
//	}

	@Override
	public TupNode visitTUP(final TUPContext ctx) {
		final TupNode tupNode = visitNar(ctx.tp());
		return tupNode;
	}

	@Override
	public TupNode visitTX(final TXContext ctx) {

		final TupNode tupNode = visitNar(ctx.tp());
		final SingNode singNode = visitNar(ctx.sing());
		final TupNode tup = new TupNode(singNode.getState(), Optional.of(tupNode));
		return tup;
	}

	@Override
	public SingNode visitSing(final SingContext ctx) {

		final boolean trueState= ctx.getChild(0) == ctx.T();
		final SingNode sing = new SingNode(trueState);
		return sing;
	}

}