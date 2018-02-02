/**
 * 
 */
package org.sievos.lexmodel.impl;

import java.util.BitSet;

class NTuple {

	final BitSet bitset;
	final int numBits;

	NTuple(final int numBits) {
		this(numBits, new BitSet(numBits));
	}

	/**
	 * @param bitSet
	 */
	public NTuple(final int numBits, final BitSet bitSet) {
		this.numBits = numBits;
		this.bitset = bitSet;
	}

	@Override
	public String toString() {
		return NTuple.bundleToString(bitset, numBits);
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
}