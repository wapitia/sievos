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

import org.antlr.v4.runtime.CharStreams;
import org.antlr.v4.runtime.CommonTokenStream;
import org.antlr.v4.runtime.tree.ParseTree;
import org.sievos.lex.SievosLexer;
import org.sievos.lex.SievosParser;
import org.sievos.lex.SievosVisitor;
import org.sievos.lexmodel.sp1.ExprLN;
import org.sievos.lexmodel.sp1.SP1;
import org.sievos.lexmodel.sp1.SP1.Executable;
import org.sievos.lexmodel.sp1.SP1Node;
import org.sievos.lexmodel.sp1.SP1NodeProducer;
import org.sievos.lexmodel.std.StdPart;

/**
 *
 */
public class SP1AntrlCompiler<LNTYPE extends SP1Node> implements SP1.Compiler {

	private final AbstractCallVisit<LNTYPE> bindVisit;

//	private final SievosVisitor<SP1Node> comp;

//	pri


//    def funcallExpr(fcall: CompositeFunctionLN)
//}
//*/
//expr        : fcall             # funcallExpr

	/**
	 * @param instance
	 * @return
	 */
	public static SP1AntrlCompiler<ExprLN> makeExprCompiler(
		final SP1NodeProducer nodeProducer)
	{
		final BindExprVisit bindVisit = new BindExprVisit(
			new SP1AntlrVisitor(nodeProducer));
		return new SP1AntrlCompiler<ExprLN>(bindVisit);
	}

	public SP1AntrlCompiler(final AbstractCallVisit<LNTYPE> bindVisit) {
		this.bindVisit = bindVisit;
	}

	/**
	 *
	 * @param expression
	 * @return
	 * @see org.sievos.lexmodel.std.StdCompiler#compile(java.lang.String)
	 */
	@Override
	public SP1.Executable compile(final String expression) {

		final SievosLexer lexer = new SievosLexer(CharStreams.fromString(expression));
        final SievosParser parser = new SievosParser(new CommonTokenStream(lexer));
        final LNTYPE castVisit = bindVisit.castVisit(parser);
        final String answerStr = castVisit.toString();
        System.out.printf("%s = %s\n", expression, answerStr);
        final Executable result = bindVisit.bindAsFunction(castVisit);
        return result;
    }

	public static abstract class AbstractCallVisit<LNTYPE extends SP1Node> {

		final SievosVisitor<SP1Node> comp;

		protected AbstractCallVisit(final SievosVisitor<SP1Node> comp)
		{
			this.comp = comp;
		}

		public LNTYPE castVisit(final SievosParser parser) {
			final ParseTree parseTree = getParseTree(parser);
			final SP1Node answer = comp.visit(parseTree);
	        @SuppressWarnings("unchecked")
			final LNTYPE f = (LNTYPE) answer;
	        return f;
		}

		public abstract Executable bindAsFunction(LNTYPE castVisit);

		public abstract ParseTree getParseTree(final SievosParser parser);

	}
	
	static abstract class FAbsImpl<SLNTYPE extends SP1Node> implements Executable {
		final SLNTYPE expr;

		protected FAbsImpl(final SLNTYPE expr) {
			this.expr = expr;
		}

		@Override
		public StdPart execute() {

			return concreteResult(expr);
		}
		
		public String toString() {
			return expr.toString(); 
		}
		

		public abstract StdPart concreteResult(SLNTYPE expr);

	}

	public static class BindExprVisit extends AbstractCallVisit<ExprLN> {

		BindExprVisit(final SievosVisitor<SP1Node> comp) {
			super(comp);
		}

		@Override
		public Executable bindAsFunction(final ExprLN expr) {
			return new FImpl(expr);
		}

		@Override
		public ParseTree getParseTree(final SievosParser parser) {
			return parser.expr();
		}

		static class FImpl extends FAbsImpl<ExprLN> {

			public FImpl(final ExprLN expr) {
				super(expr);
			}

			@Override
			public StdPart concreteResult(final ExprLN expr) {
				StdPart result = expr.asExecutable().execute();
				return result;
			}
			
		}
	}
}
