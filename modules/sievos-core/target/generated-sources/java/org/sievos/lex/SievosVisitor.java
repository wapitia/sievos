// Generated from org\sievos\lex\Sievos.g4 by ANTLR 4.7
package org.sievos.lex;
import org.antlr.v4.runtime.tree.ParseTreeVisitor;

/**
 * This interface defines a complete generic visitor for a parse tree produced
 * by {@link SievosParser}.
 *
 * @param <T> The return type of the visit operation. Use {@link Void} for
 * operations with no return type.
 */
public interface SievosVisitor<T> extends ParseTreeVisitor<T> {
	/**
	 * Visit a parse tree produced by {@link SievosParser#t1}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitT1(SievosParser.T1Context ctx);
	/**
	 * Visit a parse tree produced by the {@code TUP2}
	 * labeled alternative in {@link SievosParser#t2}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitTUP2(SievosParser.TUP2Context ctx);
	/**
	 * Visit a parse tree produced by the {@code TUP3}
	 * labeled alternative in {@link SievosParser#t3}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitTUP3(SievosParser.TUP3Context ctx);
}