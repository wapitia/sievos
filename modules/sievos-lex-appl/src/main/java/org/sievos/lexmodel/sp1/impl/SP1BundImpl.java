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

import java.util.BitSet;
import java.util.Iterator;
import java.util.Optional;

import org.sievos.kern.TI;
import org.sievos.lexmodel.std.BundLN;

import com.wapitia.common.collections.OptionalIterable;

public class SP1BundImpl extends SP1SingleImpl implements BundLN, Iterable<TI>
{
    static final OptionalIterable<TI,SP1BundImpl> iter =
        new OptionalIterable<TI,SP1BundImpl>(
            SP1BundImpl::getState, SP1BundImpl::getNext);

    private final Optional<SP1BundImpl> next;

    public SP1BundImpl(final TI state,
        final Optional<SP1BundImpl> next)
    {
        super(state);
        this.next = next;
    }


    public Optional<SP1BundImpl> getNext() {
        return next;
    }

    @Override
    public Iterator<TI> iterator() {
        return iter.iterator(this);
    }

    public SP1BundAccum makeBundAccum() {
        final BitSet bits = asBits();
        return new SP1BundAccum(size(), bits);
    }

    /**
     * An ordered set of bits, as a BitSet.
     * The {@link #getState() state } of this {@code TupNode}
     * is bit 0, the next tuple's state is
     *
     * @return a BitSet of this
     */
    public BitSet asBits() {
        final BitSet bits = new BitSet();
        setbitR(bits, size()-1);
        return bits;
    }

    public int size() {
        return 1 + next.map(SP1BundImpl::size).orElse(0);
    }

    void setbitR(final BitSet bitset, final int ix) {

        if (getState() == TI.T) {
            bitset.set(ix);
        }
        next.ifPresent(nn -> nn.setbitR(bitset, ix-1));
    }

    @Override
    public String toString() {
        return SP1BundAccum.bundleToString(asBits(), size());
    }

    @Override
    public BundLN pipe(final BundLN tbund) {
        // TODO Auto-generated method stub
        return null;
    }

}