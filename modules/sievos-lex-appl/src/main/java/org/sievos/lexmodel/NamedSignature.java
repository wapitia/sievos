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

import java.util.Objects;

/**
 * Named Function Type signature.
 * <p>
 * The signature is suitable for use as a Map key, so this
 * class is immutable and provides nicely overwritten
 * equals and hashCode methods.
 */
public class NamedSignature extends Signature {

    public static NamedSignature of(final String funcName) {
        return new NamedSignature(funcName, EMPTY_PARAMETERS);
    }

    public static NamedSignature of(final String funcName,
            final NamedSignature[] params) {
        return new NamedSignature(funcName, params);
    }

    private final String name;

    public NamedSignature(final String name, final NamedSignature[] params)
    {
        super(params);
        Objects.requireNonNull(name);
        this.name = name;
    }

    public String getName() {
        return name;
    }

    @Override
    public boolean equals(final Object o) {
        if (o == null || !(o instanceof NamedSignature)) {
            return false;
        }
        else if (o == this) {
            return true;
        }
        else {
            final NamedSignature other = (NamedSignature) o;
            return this.name.equals(other.name) &&
                    super.equals(other);
        }
    }

    @Override
    public int hashCode() {
        return 31 + (name.hashCode() << 5) + super.hashCode();
    }

    @Override
    public String toString() {
        final String result;
        if (parameters().length == 0) {
            result = name;
        }
        else {
            result = new StringBuilder()
                .append(name)
                .append(super.toString())
                .toString();
        }
        return result;
    }

}