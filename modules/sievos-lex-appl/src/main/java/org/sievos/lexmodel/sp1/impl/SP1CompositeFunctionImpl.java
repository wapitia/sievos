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

import java.util.ArrayList;

import org.sievos.lexmodel.std.CompositeFunctionLN;
import org.sievos.lexmodel.std.StdCompositeExecutable;
import org.sievos.lexmodel.std.StdPartFunction;
import org.sievos.lexmodel.std.StdPartProvider;
import org.sievos.lexmodel.std.impl.StdPartImpl;

public class SP1CompositeFunctionImpl implements CompositeFunctionLN
{

    private final java.util.List<StdPartFunction> funcList;
    private final SP1BundAccum tuple;

    public SP1CompositeFunctionImpl(
            final java.util.List<StdPartFunction> funcList,
            final SP1BundAccum tuple)
    {
        this.funcList = new ArrayList<>(funcList);
        this.tuple = tuple;
    }

    @Override
    public StdPartProvider execute() {
        final StdCompositeExecutable exec = new StdCompositeExecutable(asPart(), funcList);
        final StdPartImpl result = exec.execute();
        return result;
    }


    @Override
    public StdPartProvider asPart() {
        return tuple.asPartition();
    }

    @Override
    public java.util.List<StdPartFunction> getFuncList() {
        return funcList;
    }

    public SP1BundAccum getTuple() {
        return tuple;
    }

    @Override
    public String toString() {

        final StringBuilder bldr = new StringBuilder();
        bldr.append(tuple.toString());
        bldr.append(' ');
        if (funcList.size() == 1) {
            bldr.append(funcList.get(0).toString());
        }
        else {
            bldr.append(funcList.toString());
        }
        return bldr.toString();
    }


}