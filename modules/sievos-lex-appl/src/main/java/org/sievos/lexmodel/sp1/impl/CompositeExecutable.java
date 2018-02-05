package org.sievos.lexmodel.sp1.impl;

import java.util.List;

import org.sievos.kern.Part;
import org.sievos.lexmodel.sp1.SP1;
import org.sievos.lexmodel.std.StdBund;
import org.sievos.lexmodel.std.StdPart;

/**
 * Executable takes a Standard Partition and a list of Partion Functions
 * and applies the composite function chain to each bundle of the given
 * partition.
 */
public class CompositeExecutable implements SP1.Executable {

    final StdPart inputPart;
    final List<SP1.PartFunction> funcList;

    CompositeExecutable(final StdPart inputPart, final List<SP1.PartFunction> funcList) {
        this.inputPart = inputPart;
        this.funcList = funcList;

    }

    @Override
    public StdPartImpl execute() {

        final Part<StdBund> part = inputPart.partition();
        final Part<StdBund> resultPart = part.map(bund -> composeEachBund(bund));
        final StdPartImpl result = new StdPartImpl(resultPart);
        return result;
    }

    StdBund composeEachBund(final StdBund bund) {
        StdBund accumBund = bund;
        for (final SP1.PartFunction pf : funcList) {
            accumBund = pf.execute(accumBund);
        }
        return accumBund;
    }
}