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

import java.util.function.Function;

import org.antlr.v4.runtime.CharStreams;
import org.antlr.v4.runtime.CodePointCharStream;
import org.antlr.v4.runtime.CommonTokenStream;
import org.antlr.v4.runtime.tree.ParseTree;
import org.sievos.lex.SievosLexer;
import org.sievos.lex.SievosParser;
import org.sievos.lex.SievosVisitor;
import org.sievos.lexmodel.Executable;
import org.sievos.lexmodel.sp1.ExprLN;
import org.sievos.lexmodel.sp1.SP1Node;
import org.sievos.lexmodel.sp1.SP1NodeProducer;
import org.sievos.lexmodel.std.StdCompiler;

/**
 *
 */
public class SP1AntrlCompiler<N extends SP1Node,R> implements StdCompiler<R> {

    /**
     * Make compiler that takes a Sieveos "expr" goal and produces some
     * Executable from it.
     */
    public static SP1AntrlCompiler<ExprLN,Executable> makeExprCompiler(
        final SP1NodeProducer nodeProducer)
    {
		final SP1AntlrVisitor comp = new SP1AntlrVisitor(nodeProducer);
		// ExprLN is both the result from the parse tree visit as well
		// as the result executable, making these supplied functions easy
        return new SP1AntrlCompiler<ExprLN,Executable>(comp,
        	SievosParser::expr,
        	(final SP1Node n) -> (ExprLN) n,
        	(final ExprLN n) -> n);
    }

	private final SievosVisitor<SP1Node> antlrCompiler;
    // the goal being some parse tree
    private final Function<SievosParser,ParseTree> goalOfParser;
    private final Function<SP1Node,N> narrowNode;
    private final Function<N,R> nodeToResult;

    // Fully loaded constructor
    protected SP1AntrlCompiler(
    	final SievosVisitor<SP1Node> antlrCompiler,
    	final Function<SievosParser,ParseTree> goalOfParser,
        final Function<SP1Node,N> narrowNodeFunc,
        final Function<N,R> nodeToResult)
    {
    	this.antlrCompiler = antlrCompiler;
        this.goalOfParser = goalOfParser;
        this.narrowNode = narrowNodeFunc;
        this.nodeToResult = nodeToResult;
    }

    /**
     *
     * @param expression
     * @return
     * @see org.sievos.lexmodel.std.StdCompiler#compile(java.lang.String)
     */
    @Override
    public R compile(final String expression) {

    	// one-shot use for these things, i guess
        final CodePointCharStream cpcinput = CharStreams.fromString(expression);
		final SievosLexer lexer = new SievosLexer(cpcinput);
        final CommonTokenStream tokens = new CommonTokenStream(lexer);
		final SievosParser parser = new SievosParser(tokens);

        // get the parsed tree from a parser goal such as parser.expr()
        final ParseTree parseTree = goalOfParser.apply(parser);
   		final SP1Node answer = antlrCompiler.visit(parseTree);
        final N node = narrowNode.apply(answer);
        final String answerStr = node.toString();
        System.out.printf("%s = %s\n", expression, answerStr);
        final R executable  = nodeToResult.apply(node) ;
        return executable;
    }

}
