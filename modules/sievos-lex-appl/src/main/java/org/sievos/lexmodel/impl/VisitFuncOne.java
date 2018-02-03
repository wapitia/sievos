package org.sievos.lexmodel.impl;

import static org.sievos.lexmodel.impl.LexNodeMenagerie.makeCompositeFunction;
import static org.sievos.lexmodel.impl.LexNodeMenagerie.makeIdentifier;
import static org.sievos.lexmodel.impl.LexNodeMenagerie.makeTBundle;
import static org.sievos.lexmodel.impl.LexNodeMenagerie.makeTSingle;

import org.antlr.v4.runtime.tree.AbstractParseTreeVisitor;
import org.antlr.v4.runtime.tree.ParseTree;
import org.sievos.lex.SievosParser;
import org.sievos.lex.SievosParser.SingContext;
import org.sievos.lex.SievosParser.TUPContext;
import org.sievos.lex.SievosParser.TXContext;
import org.sievos.lex.SievosVisitor;
import org.sievos.lexmodel.impl.LexNodeMenagerie.CompositeFunctionLN;
import org.sievos.lexmodel.impl.LexNodeMenagerie.IdentifierLN;
import org.sievos.lexmodel.impl.LexNodeMenagerie.TBundLN;
import org.sievos.lexmodel.impl.LexNodeMenagerie.TSingleLN;

public class VisitFuncOne extends AbstractParseTreeVisitor<LexNode>
	implements SievosVisitor<LexNode>
{
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
	public CompositeFunctionLN visitSINGF(final SievosParser.SINGFContext ctx) {

		final TBundLN ptp = visitNar(ctx.ptp());
		final IdentifierLN funcName = visitNar(ctx.func());
		return makeCompositeFunction(funcName, ptp);
	}

	@Override
	public CompositeFunctionLN visitMULTF(final SievosParser.MULTFContext ctx) {

		final CompositeFunctionLN subfunc = visitNar(ctx.fcall());
		final IdentifierLN funcName = visitNar(ctx.func());
		return makeCompositeFunction(funcName, subfunc);
	}

	//		func      : IDENT
	//	    ;
	@Override
	public IdentifierLN visitFunc(final SievosParser.FuncContext ctx) {

		final ParseTree child = ctx.getChild(0);
		final String ident = child.getText();
		return makeIdentifier(ident);
	}

	//		// rule ptp
	//		// <tuple>
	//		// a single FTFTuple or other FTFTuple stream representation which can be
	//		// applied against a composite functional block
	//		//
	//		ptp       : tp             # TUP
	//		          ;
	@Override
	public TBundLN visitTUP(final TUPContext ctx) {
		final TBundLN tbund = visitNar(ctx.tp());
		return tbund;
	}

	//		// rule tp
	//		// <T|F>...
	//		//
	//		// example: 'TFT'
	//
	//		tp        : sing           # T1
	//		          | sing tp        # TX
	//		          ;
	@Override
	public TBundLN visitT1(final SievosParser.T1Context ctx) {

		final TSingleLN singNode = visitNar(ctx.sing());
		return makeTBundle(singNode);
	}

	@Override
	public TBundLN visitTX(final TXContext ctx) {

		final TBundLN tupNode = visitNar(ctx.tp());
		final TSingleLN singNode = visitNar(ctx.sing());
		return makeTBundle(singNode, tupNode);
	}

	@Override
	public TSingleLN visitSing(final SingContext ctx) {

		final boolean trueState = ctx.getChild(0) == ctx.T();
		return makeTSingle(trueState);
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

}