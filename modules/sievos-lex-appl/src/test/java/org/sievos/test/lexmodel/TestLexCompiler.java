package org.sievos.test.lexmodel;

import java.util.Arrays;
import java.util.List;
import java.util.function.Consumer;
import java.util.function.Function;

import org.junit.Assert;
import org.junit.Test;
import org.sievos.lexmodel.Executable;
import org.sievos.lexmodel.SievosLexTool;
import org.sievos.lexmodel.std.StdPartProvider;


/**
 *
 */
public class TestLexCompiler {

    @Test
    public void testOne() {

        final List<Tup2<String,String>> expressions = Arrays.asList(
            ee("FTT r z","TTF"));
        expressions.forEach(compareFunc);
    }
    @Test
    public void testSimple() {

        final List<Tup2<String,String>> expressions = Arrays.asList(
            ee("TTF r", "FTT"),
            ee("FTT r z", "TTF"),
            ee("FFF r", "FFF"),
            ee("TTT z", "TTT"),
            ee("TFT r", "TTF"),
            ee("TFF z", "TFF"));
        expressions.forEach(compareFunc);
    }

    static class Tup2<I,E> {
        I i; E e;
        Tup2(final I expr, final E expect)
        {this.i = expr; this.e = expect; }
    }

    static Tup2<String,String> ee(final String expr, final String expect) {
        return new Tup2<String,String>(expr, expect);
    }

    Consumer<Tup2<String,String>> compareFunc =
        ee -> funcCompare(ee.i, ee.e);

    Function<String,Executable> compileFunc =
        SievosLexTool::compile;

    void funcCompare(final String s, final String x) {
        final Executable apply = compileFunc.apply(s);
        final StdPartProvider result = apply.execute();
        final String resultString = result.toString();
        System.out.println(String.format("%-20s -> %s -> %s",
            s, apply.toString(), resultString));
        Assert.assertEquals("Mismatched result", x, resultString);
    };

}
