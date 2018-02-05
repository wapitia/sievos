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

package org.sievos.lexmodel;

import java.util.Arrays;
import java.util.Objects;
import java.util.stream.Collectors;

/**
 * Named Function Type signature
 */
public class NFT {

	public static NFT of(final String funcName) {
		return new NFT(funcName, EMPTY_PARAMETERS);
	}

	static final NFT[] EMPTY_PARAMETERS =
		new NFT[0];

	private final String name;
	private final NFT[] params;

	public NFT(final String name, final NFT[] params)
	{
		Objects.requireNonNull(name);
		Objects.requireNonNull(params);
		this.name = name;
		this.params = params;
	}

	public String getName() {
		return name;
	}

	public NFT[] getParameters() {
		return params;
	}

	@Override
	public boolean equals(final Object o) {
		if (o == null || o.getClass() != this.getClass()) {
			return false;
		}
		else if (o == this) {
			return true;
		}
		else {
			final NFT other = (NFT) o;
			return this.name.equals(other.name) &&
				Arrays.equals(this.params, other.params);
		}
	}

	@Override
	public int hashCode() {
		return Objects.hash(name, params);
	}

	@Override
	public String toString() {
		final String result;
		if (params.length == 0) {
			result = name;
		}
		else {
			final StringBuilder bldr = new StringBuilder()
				.append(name)
				.append(Arrays.asList(params).stream()
					.map(Object::toString)
					.collect(Collectors.joining(",", "[", "]"))
					);
			result = bldr.toString();
		}
		return result;
	}

}