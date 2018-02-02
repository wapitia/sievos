package org.sievos.test.lexmodel;

import org.junit.Test;
import org.sievos.lexmodel.SievosLexTool;

/**
 *
 */
public class TestLexCompiler {

	@Test
	public void testSimple() {

		SievosLexTool.compile("TTF r");
		SievosLexTool.compile("FTT r z");
		SievosLexTool.compile("FFF r");
		SievosLexTool.compile("TTT z");
		SievosLexTool.compile("TFT r");
		SievosLexTool.compile("TFF z");
	}
}
