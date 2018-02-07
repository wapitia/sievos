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
import java.util.Iterator;

/**
 * Iterates the bits of a BitSet starting at bit 0 and progressing upward,
 * counting both bits and holes. Set bits are returned as Boolean.True
 * and holes are returned as Boolean.False.
 */
public class BitSetIterator implements Iterator<Boolean> {

    // BitSet to interrogate. Does not change.
    private final BitSet bitSet;
    // current BitSet bit index to report on
    private int curIndex;
    // result of a call to bitSet.nextSetBit(curIndex), the
    // index of the next bit set on or higher than curIndex,
    // or -1 if there is no higher bit on or after curIndex
    private int oneIndex;

    public BitSetIterator(final BitSet bitSet) {
        this.bitSet = bitSet;
        this.curIndex = 0;
        // prime the one index
        advanceOneIndex();
    }

    /**
     * returns true, always has next bit
     */
    @Override
    public boolean hasNext() {
        return true;
    }

    /**
     * @return the next bit as a Boolean,
     *         true if the bit set, false if the bit is clear
     *         then advances to the next bit
     */
    @Override
    public Boolean next() {
        // result is true if the current bit at curIx matches the next one bit
        final boolean currentIsSet = curIndex == oneIndex;
        if (!oneIsUnbounded()) {
            this.curIndex += 1;
            if (currentIsSet) {
                advanceOneIndex();
            }
        }
        validateInvariants();
        return currentIsSet;
    }

    static final int BIT_SET_UNBOUNDED = -1;

    // calls {@link BitSet#nextSetBit} which return -1 when there are no
    // more set bits
    final void advanceOneIndex() {
        this.oneIndex = bitSet.nextSetBit(curIndex);
    }

    final boolean oneIsUnbounded() {
        return oneIndex == BIT_SET_UNBOUNDED;
    }

    // public methods must adhere to these invariants upon exit
    final void validateInvariants() {
        assert curIndex >= 0;
        assert oneIsUnbounded() || curIndex <= oneIndex;
    }


}
