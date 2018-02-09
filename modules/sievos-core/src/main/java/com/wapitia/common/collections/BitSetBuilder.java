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

package com.wapitia.common.collections;

import java.util.BitSet;

/**
 * Build a BitSet treated as if it is a stream of boolean true and false
 * values, starting at bit 0.
 */
public class BitSetBuilder {

    /**
     * Create a BitSetBuilder optionally pre-populated with an array
     * of bit states.
     *
     * @param initBits the optional initial array of bit states
     * @return a new BitSetBuilder, from which you'll call
     */
    public static BitSetBuilder build(final boolean ... initBits) {

        final BitSet bitSet = new BitSet();
        final int fromIndex = setAll(bitSet, 0, initBits);
        final BitSetBuilder result = new BitSetBuilder(fromIndex, bitSet);
        return result;
    }

    /**
     * Builder constructor takes a BitSet that may be preset up to {@code curix}
     * bits, and the starting index from which more bits may be pushed.
     * The bits from 0 to curix-1 come before future pushed and this
     * lower range of the BitSet will not be altered
     *
     * @param curix starting index from which to push additional bits,
     *        if desired
     * @param bitSet The accumulating bitSet that ultimately will be returned
     *        by {@linkplain BitSetBuilder#make() make}.
     * @see BitSetBuilder#make
     */
    public BitSetBuilder(final int curix, final BitSet bitSet) {
        this.curix = curix;
        this.accum = bitSet;
    }

    // accumulating
    final BitSet accum;

    // for push operation
    int curix;

    /**
     * Push another rack of bits onto the accumulating BitSet where we left
     * off with the constructor or the last push
     *
     * @param moreBits even more bits to push
     * @return this builder, allowing for chained api calls.
     */
    public BitSetBuilder push(final boolean ... moreBits) {

        this.curix = setAll(accum, curix, moreBits);
        return this;
    }

    /**
     * @return the accumulated BitSet
     */
    public BitSet make() {
        return accum;
    }

    /**
     * Set or clear the array of bits starting from {@code fromIndex}
     * according to the supplied array of mapped bits from {@code bitz},
     * returning the new length.
     *
     * @param bitSet The set
     * @param fromIndex starting index, based from 0
     * @param bitz the map of set or unset bits
     * @return the new length, fromIndex + bitz.length
     */
    public static int setAll(final BitSet bitSet, final int fromIndex,
        final boolean ... bitz)
    {
        final int newlength = fromIndex + bitz.length;
        for (int ibix = fromIndex; ibix < newlength; ibix++) {
            bitSet.set(ibix, bitz[ibix]);
        }
        return newlength;
    }

}
