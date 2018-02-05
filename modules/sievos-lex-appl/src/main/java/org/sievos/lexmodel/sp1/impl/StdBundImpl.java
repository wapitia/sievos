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
package org.sievos.lexmodel.sp1.impl;

import java.util.Arrays;
import java.util.stream.Collectors;

import org.sievos.kern.TI;
import org.sievos.lexmodel.std.StdBund;

/**
 * Basic StdBund implementation
 */
public class StdBundImpl implements StdBund {

    private final TI[] tiarray;

    StdBundImpl(final TI ... src) {
        this.tiarray = new TI[src.length];
        System.arraycopy(src, 0, tiarray, 0, src.length);
    }

    /**
     * Return a copy of this Bundle's array of TI states
     */
    @Override
    public TI[] asArray() {
        final TI[] res = new TI[tiarray.length];
        System.arraycopy(tiarray, 0, res, 0, tiarray.length);
        return res;
    }

    /**
     * Return this Bundle's TI states uncopied.
     * Implementers should not change the contents of this array even though
     * possible as this class should remain unchanged.
     */
    protected TI[] tiArray() {
        return tiarray;
    }

    @Override
    public boolean equals(Object o) {
        if (o == null || !(o instanceof StdBundImpl)) {
            return false;
        }
        else if (this == o) {
            return true;
        }
        else {
            final StdBundImpl other = (StdBundImpl) o;
            return this.tiarray.equals(other.tiarray);
        }
    }

    @Override
    public int hashCode() {
        return tiarray.hashCode();
    }

    @Override
    public String toString() {
        return Arrays.asList(tiarray).stream()
            .<String> map(Object::toString)
            .collect(Collectors.joining());
    }

}