package org.sievos.test.lexmodel;

import java.util.Arrays;
import java.util.List;
import java.util.function.Consumer;
import java.util.function.Function;

import org.junit.Test;
import org.sievos.lexmodel.Executable;
import org.sievos.lexmodel.SievosLexTool;
import org.sievos.lexmodel.std.StdPartProvider;


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

    Function<String,Executable> compileFunc = (s) -> SievosLexTool.compile(s);

    Consumer<String> funcCompare = (s) -> {
        final Executable apply = compileFunc.apply(s);
        final StdPartProvider result = apply.execute();
        System.out.println(String.format("%-20s -> %s -> %s",
            s, apply.toString(), result.toString()));
    };

}
