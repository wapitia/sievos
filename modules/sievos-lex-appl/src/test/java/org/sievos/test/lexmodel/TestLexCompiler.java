package org.sievos.test.lexmodel;

import java.util.Arrays;
import java.util.List;
import java.util.function.Consumer;
import java.util.function.Function;

import org.junit.Test;
import org.sievos.lexmodel.SP1LexTool;
import org.sievos.lexmodel.sp1.SP1.Executable;
import org.sievos.lexmodel.std.StdPart;


/**
 *
 */
public class TestLexCompiler {

	@Test
	public void testSimple() {

		final List<String> expressions = Arrays.<String> asList(
			"TTF r",
			"FTT r z",
			"FFF r",
			"TTT z",
			"TFT r",
			"TFF z");
		expressions.forEach(funcCompare);
	}

	Function<String,Executable> compileFunc = (s) -> SP1LexTool.compile(s);

	Consumer<String> funcCompare = (s) -> {
		final Executable apply = compileFunc.apply(s);
		final StdPart result = apply.execute();
		System.out.println(String.format("%-20s -> %s -> %s",
			s, apply.toString(), result.toString()));
	};

}
