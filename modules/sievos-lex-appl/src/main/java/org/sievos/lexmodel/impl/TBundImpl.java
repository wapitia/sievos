/**
 *
 */
package org.sievos.lexmodel.impl;

import java.util.BitSet;

import org.sievos.lexmodel.TBund;
import org.sievos.lexmodel.TI;

class TBundImpl implements TBund {

	final BitSet bitset;
	final int numBits;

	TBundImpl(final int numBits) {
		this(numBits, new BitSet(numBits));

	}

	/**
	 * @param bitSet
	 */
	public TBundImpl(final int numBits, final BitSet bitSet) {
		this.numBits = numBits;
		this.bitset = bitSet;
	}

	@Override
	public String toString() {
		return TBundImpl.bundleToString(bitset, numBits);
	}

	public static String bundleToString(final BitSet bitset, final int size) {
		final StringBuilder bldr = new StringBuilder();
		for (int ix = size-1; ix >= 0; --ix) {
			bldr.append(tDispChar(bitset.get(ix)));
		}
		return bldr.toString();
	}

	public static char tDispChar(final boolean state) {
		return state ? 'T' : 'F';
	}

	/**
	 *
	 * @return
	 * @see org.sievos.lexmodel.TBund#asArray()
	 */
	@Override
	public TI[] asArray() {
//		TF tf = F;
		// TODO Auto-generated method stub
		return null;
	}
}