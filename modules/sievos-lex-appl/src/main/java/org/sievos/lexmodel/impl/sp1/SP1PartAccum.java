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
package org.sievos.lexmodel.impl.sp1;

import org.sievos.kern.Part;
import org.sievos.lexmodel.std.StdBund;
import org.sievos.lexmodel.std.StdPart;

/**
 * Partition of TBund accumulator for the SP1 Sievos expression tree.
 */
class SP1PartAccum {

	Part<StdBund> part;

	SP1PartAccum() {
		// start with an empty partition
		this.part = Part.apply();
	}

	public void append(final StdBund addition) {
		this.part = Part.apply(this.part, addition);
	}

	static class TPartImpl implements StdPart {

		private final Part<StdBund> part;

		TPartImpl(final Part<StdBund> part) {
			this.part = part;
		}

		@Override
		public Part<StdBund> asPartition() {
			return this.part;
		}
	}

	public TPartImpl asPart() {
		return SP1PartAccum.asPart(part);
	}

	public static TPartImpl asPart(final Part<StdBund> state) {
		return new TPartImpl(state);
	}

}
