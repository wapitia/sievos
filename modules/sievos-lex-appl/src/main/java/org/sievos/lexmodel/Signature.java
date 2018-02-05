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
 * Unnamed Function Type signature.
 * <p>
 * The signature is suitable for use as a Map key, so this
 * class is immutable and provides nicely overwritten
 * equals and hashCode methods.
 */
public class Signature {

    public static final NamedSignature[] EMPTY_PARAMETERS =
        new NamedSignature[0];

    private final Signature[] params;

    public static Signature of(final Signature[] params) {
        return new Signature(params);
    }

    public Signature(final Signature[] params)
    {
        Objects.requireNonNull(params);
        this.params = params;
    }

    /**
     * Copy the signatures to an output array.
     * @return
     */
    public Signature[] getParameters() {
        final Signature[] result = new Signature[params.length];
        System.arraycopy(params, 0, result, 0, params.length);
        return result;
    }

    @Override
    public boolean equals(final Object o) {
        if (o == null || ! (o instanceof Signature)) {
            return false;
        }
        else if (o == this) {
            return true;
        }
        else {
            final Signature other = (Signature) o;
            return Arrays.equals(this.params, other.params);
        }
    }

    @Override
    public int hashCode() {
        return params.hashCode();
    }

    @Override
    public String toString() {

        final StringBuilder bldr = new StringBuilder()
            .append(Arrays.asList(params).stream()
                .map(Object::toString)
                .collect(Collectors.joining(",", "[", "]"))
                );
        return bldr.toString();
    }

    /**
     * Protected direct interface to the parameters array, without copying them
     * first. Users should not change the contents of the array, even though
     * they could.
     */
    protected Signature[] parameters() {
        return params;
    }
}
