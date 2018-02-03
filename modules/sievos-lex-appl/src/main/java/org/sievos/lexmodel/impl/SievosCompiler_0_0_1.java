/**
 *
 */
package org.sievos.lexmodel.impl;

import org.antlr.v4.runtime.CharStreams;
import org.antlr.v4.runtime.CommonTokenStream;
import org.antlr.v4.runtime.tree.ParseTree;
import org.sievos.lex.SievosLexer;
import org.sievos.lex.SievosParser;
import org.sievos.lex.SievosVisitor;
import org.sievos.lexmodel.LexResult;
import org.sievos.lexmodel.SievosCompiler;

/**
 *
 */
public class SievosCompiler_0_0_1 implements SievosCompiler {

	private final SievosVisitor<LexNode> comp;

	public SievosCompiler_0_0_1() {
		this.comp = new VisitFuncOne();
	}

	/**
	 *
	 * @param expression
	 * @return
	 * @see org.sievos.lexmodel.SievosCompiler#compile(java.lang.String)
	 */
	@Override
	public LexResult compile(final String expression) {

        final ParseTree tree = makeParseTree(expression);
        final LexNode answer = comp.visit(tree);
        final LexNodeMenagerie.CompositeFunctionLN f = (LexNodeMenagerie.CompositeFunctionLN) answer;
        final String answerStr = f.toString();
        System.out.printf("%s = %s\n", expression, answerStr);
        // TODO
        return null;
    }

	public ParseTree makeParseTree(final String expression) {
		final SievosLexer lexer = new SievosLexer(CharStreams.fromString(expression));
        final SievosParser parser = new SievosParser(new CommonTokenStream(lexer));
        final ParseTree tree = parser.fcall();
		return tree;
	}

}
