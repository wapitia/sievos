/**
 *
 */
package org.sievos.lexmodel;

import org.sievos.lexmodel.impl.SievosCompiler_0_0_1;

/**
 *
 */
public interface SievosLexTool {

	static LexResult compile(final String expression) {
		return compilerInstance.compile(expression);
	}

	static SievosCompiler compilerInstance = new SievosCompiler_0_0_1();

}
