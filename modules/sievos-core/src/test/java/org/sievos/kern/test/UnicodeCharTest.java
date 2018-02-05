package org.sievos.kern.test;

import java.io.PrintStream;

import org.junit.Test;

/**
 *
 */
public class UnicodeCharTest {

    @Test
    public void testSymbolOut() {
        try {
            final char WHITE_CIRCLE = 0x25CB;
            final char BLACK_CIRCLE = 0x25CF;

            final PrintStream out = System.out;
            out.print(WHITE_CIRCLE);
            out.print(BLACK_CIRCLE);
            out.close();
        }
        catch (final Throwable e) {
            e.printStackTrace(System.out);
        }
    }
}
