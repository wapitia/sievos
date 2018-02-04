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

import org.antlr.v4.runtime.CharStreams;
import org.antlr.v4.runtime.CommonTokenStream;
import org.antlr.v4.runtime.tree.ParseTree;
import org.sievos.lex.SievosLexer;
import org.sievos.lex.SievosParser;
import org.sievos.lex.SievosVisitor;
import org.sievos.lexmodel.sp1.CompositeFunctionLN;
import org.sievos.lexmodel.sp1.SP1Node;
import org.sievos.lexmodel.sp1.SP1NodeProducer;
import org.sievos.lexmodel.std.StdCompiler;
import org.sievos.lexmodel.std.StdResult;

/**
 *
 */
public class SP1AntrlCompiler implements StdCompiler {

	private final SievosVisitor<SP1Node> comp;

	public SP1AntrlCompiler(final SP1NodeProducer nodeProducer) {
		this.comp = new SP1AntlrVisitor(nodeProducer);
	}

	/**
	 *
	 * @param expression
	 * @return
	 * @see org.sievos.lexmodel.std.StdCompiler#compile(java.lang.String)
	 */
	@Override
	public StdResult compile(final String expression) {

        final ParseTree tree = makeParseTree(expression);
        final SP1Node answer = comp.visit(tree);
        final CompositeFunctionLN f = (CompositeFunctionLN) answer;
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
