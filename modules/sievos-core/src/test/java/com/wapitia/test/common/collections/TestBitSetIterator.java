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
package com.wapitia.test.common.collections;

import java.util.BitSet;
import java.util.Iterator;

import org.junit.Test;

import com.wapitia.common.collections.BitSetBuilder;
import com.wapitia.common.collections.WapitiaCollections;

public class TestBitSetIterator {

	@Test
	public void testBitSet1() {
		final BitSet bitSetz = BitSetBuilder.build(true).make();
		outBitSetIter(bitSetz);

		final BitSet bitSet1 = BitSetBuilder.build(false, true).make();
		outBitSetIter(bitSet1);

		final BitSet bitSeta = BitSetBuilder.build(false, true, false, false, false, false, true,
			false, false, false, true, true, false, true, false, true).make();
		outBitSetIter(bitSeta);
	}

	private String outBitSetIter(final BitSet bitSet0) {
		final StringBuilder bldr = new StringBuilder();
		final Iterator<Boolean> iter = WapitiaCollections.bitSetIterator(bitSet0);
		for (int i = 0; i < 20; i++) {
			final boolean n = iter.next();
			System.out.print(n ? "T": "F");
		}
		System.out.println();
		return bldr.toString();
	}
}

