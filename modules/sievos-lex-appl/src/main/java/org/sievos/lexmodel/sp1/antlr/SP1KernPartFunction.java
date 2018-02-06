/*
 * Copyright 2016-2018 wapitia.com
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
package org.sievos.lexmodel.sp1.antlr;

import java.util.function.Function;

import org.sievos.kern.Kern;
//import org.sievos.kern.Kern$.N;
import org.sievos.kern.TI;
import org.sievos.lexmodel.sp1.impl.StdBundImpl;
import org.sievos.lexmodel.std.StdBund;
import org.sievos.lexmodel.std.StdPartFunction;

class SP1KernPartFunction implements StdPartFunction {

    private final String name;
    private final Function<Kern.N,Kern.N> n2n;

    public SP1KernPartFunction(final String funcName, final Function<Kern.N,Kern.N> n2n) {
        this.name = funcName;
        this.n2n = n2n;
    }

    public Kern.N bund2KernN(final StdBund bund) {

        final TI b = TI.F;
        final TI y = TI.T;
        final TI x = TI.T;
        final Kern.N n = new Kern.N(b,y,x);
        return n;
    }

    public StdBund kern2Bund(final Kern.N n) {
        final StdBundImpl result = StdBundImpl.apply(n.b(),n.y(),n.x());
        return result;
    }

    @Override
    public StdBund execute(final StdBund bund) {
        return kern2Bund(n2n.apply(bund2KernN(bund)));
    }

    @Override
    public String toString() {
        return name;
    }

}