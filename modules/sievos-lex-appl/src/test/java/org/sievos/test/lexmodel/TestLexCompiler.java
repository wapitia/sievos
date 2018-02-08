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
package org.sievos.test.lexmodel;

import java.util.Arrays;
import java.util.List;
import java.util.function.Consumer;
import java.util.function.Function;

import org.sievos.lexmodel.Executable;
import org.sievos.lexmodel.SievosLexTool;
import org.sievos.lexmodel.std.StdPartProvider;

import org.junit.Assert;
import org.junit.Test;

import com.wapitia.lex.StdGenerateStatus;

import scala.Option;
import scala.Tuple2;

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
    public void testInvalid() {
        final List<Tup2<String,String>> expressions = Arrays.asList(
            ee("X","TTF"));
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

    Function<String,Tuple2<Option<Executable>,StdGenerateStatus>> compileFunc =
        SievosLexTool::generate;

    void funcCompare(final String s, final String x) {
        final Tuple2<Option<Executable>,StdGenerateStatus> apply = compileFunc.apply(s);
        final Option<Executable> optEx =  apply._1();
        final StdGenerateStatus genStatus = apply._2();
        System.out.println("Status: " + genStatus);
        if (optEx.isDefined()) {
            final StdPartProvider result = optEx.get().execute();
            final String resultString = result.toString();
            System.out.println(String.format("%-20s -> %s -> %s",
                s, apply.toString(), resultString));
            Assert.assertEquals("Mismatched result", x, resultString);
        }
    };

}
