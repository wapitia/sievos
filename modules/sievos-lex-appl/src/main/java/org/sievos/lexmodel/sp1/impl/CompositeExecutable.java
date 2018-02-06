package org.sievos.lexmodel.sp1.impl;

import java.util.List;

import org.sievos.kern.Part;
import org.sievos.lexmodel.Executable;
import org.sievos.lexmodel.std.StdBund;
import org.sievos.lexmodel.std.StdPartFunction;
import org.sievos.lexmodel.std.StdPartProvider;

/**
 * Executable takes a Standard Partition and a list of Partion Functions
 * and applies the composite function chain to each bundle of the given
 * partition.
 */
public class CompositeExecutable implements Executable {

    final StdPartProvider inputPart;
    final List<StdPartFunction> funcList;

    CompositeExecutable(final StdPartProvider inputPart,
            final List<StdPartFunction> funcList)
    {
        this.inputPart = inputPart;
        this.funcList = funcList;
    }

    @Override
    public StdPartImpl execute() {

        final Part<StdBund> part = inputPart.partition();
        final Part<StdBund> resultPart =
                part.map(bund -> composeEachBund(bund));
        final StdPartImpl result = new StdPartImpl(resultPart);
        return result;
    }

    StdBund composeEachBund(final StdBund bund) {
        StdBund accumBund = bund;
        for (final StdPartFunction pf : funcList) {
            accumBund = pf.execute(accumBund);
        }
        return accumBund;
    }
}