package org.sievos.lexmodel.sp1.impl;

import org.sievos.lex.SievosParser;
import org.sievos.lex.SievosVisitor;
import org.sievos.lexmodel.sp1.SP1.Executable;
import org.sievos.lexmodel.sp1.SP1Node;

import org.antlr.v4.runtime.tree.ParseTree;

public abstract class AbstractCallVisit<LNTYPE extends SP1Node> {

    final SievosVisitor<SP1Node> comp;

    protected AbstractCallVisit(final SievosVisitor<SP1Node> comp)
    {
        this.comp = comp;
    }

    public LNTYPE castVisit(final SievosParser parser) {
        final ParseTree parseTree = getParseTree(parser);
        final SP1Node answer = comp.visit(parseTree);
        @SuppressWarnings("unchecked")
        final LNTYPE f = (LNTYPE) answer;
        return f;
    }

    public abstract Executable bindAsFunction(LNTYPE castVisit);

    public abstract ParseTree getParseTree(final SievosParser parser);

}