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
package org.sievos.lexmodel.sp1.antlr;

import java.util.ArrayList;
import java.util.BitSet;
import java.util.EnumMap;
import java.util.Iterator;
import java.util.List;

import org.sievos.kern.Part;
import org.sievos.kern.TI;
import org.sievos.lexmodel.sp1.impl.StdBundImpl;
import org.sievos.lexmodel.std.StdBund;
import org.sievos.lexmodel.std.StdPartProvider;

import com.wapitia.common.collections.WapitiaCollections;

class SP1BundAccum {

    public static String tDispChar(final TI ti) {
        return TI_DISP_MAP.get(ti);
    }

    static EnumMap<TI,String> TI_DISP_MAP = new EnumMap<TI,String>(TI.class);
    static {
        TI_DISP_MAP.put(TI.F, "F");
        TI_DISP_MAP.put(TI.T, "T");
    }

    final BitSet bitset;
    final int numBits;

    SP1BundAccum(final int numBits) {
        this(numBits, new BitSet(numBits));
    }

    /**
     * @param bitSet
     */
    public SP1BundAccum(final int numBits, final BitSet bitSet) {
        this.numBits = numBits;
        this.bitset = bitSet;
    }

    @Override
    public String toString() {
        return SP1BundAccum.asBund(bitset, numBits).toString();
    }

    StdPartProvider asPartition() {
        final Part<StdBund> result = Part.apply(SP1BundAccum.asBund(bitset, numBits));
        return () -> result;
    }

    public static String bundleToString(final BitSet bitset, final int size) {

        final StringBuilder bldr = new StringBuilder();
        final Iterator<Boolean> iter = WapitiaCollections.bitSetIterator(bitset,size);
        iter.forEachRemaining( (final Boolean bitvalue) -> {
	    		final TI tWhen = TI.toTWhen(bitvalue);
	    		bldr.append(tDispChar(tWhen));
	         }
        );
        return bldr.toString();
    }

    public static StdBund asBund(final BitSet bitset, final int size) {
        final List<TI> tilist = new ArrayList<>();
        final Iterator<Boolean> iter = WapitiaCollections.bitSetIterator(bitset,size);
        iter.forEachRemaining( (final Boolean bitvalue) -> {
	    		final TI tWhen = TI.toTWhen(bitvalue);
	            tilist.add(0, tWhen);
	         }
        );
        final StdBundImpl result = new StdBundImpl(tilist.toArray(new TI[size]));
        return result;
    }

}