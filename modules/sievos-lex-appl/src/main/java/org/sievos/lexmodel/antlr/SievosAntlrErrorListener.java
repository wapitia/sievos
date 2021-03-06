package org.sievos.lexmodel.antlr;

import java.util.BitSet;

import org.antlr.v4.runtime.ANTLRErrorListener;
import org.antlr.v4.runtime.Parser;
import org.antlr.v4.runtime.RecognitionException;
import org.antlr.v4.runtime.Recognizer;
import org.antlr.v4.runtime.atn.ATNConfigSet;
import org.antlr.v4.runtime.dfa.DFA;

public class SievosAntlrErrorListener implements ANTLRErrorListener {

    @Override
    public void syntaxError(Recognizer<?, ?> recognizer,
            Object offendingSymbol, int line, int charPositionInLine,
            String msg, RecognitionException e)
    {
        System.out.println("Syntax error: " + msg);
    }

    @Override
    public void reportAmbiguity(Parser recognizer, DFA dfa, int startIndex,
            int stopIndex, boolean exact, BitSet ambigAlts,
            ATNConfigSet configs)
    {
        System.out.println("Ambiguity: " + ambigAlts.toString());
    }

    @Override
    public void reportAttemptingFullContext(Parser recognizer, DFA dfa,
            int startIndex, int stopIndex, BitSet conflictingAlts,
            ATNConfigSet configs)
    {
        System.out.println("AttemptingFullContext: " + dfa.toString());
    }

    @Override
    public void reportContextSensitivity(Parser recognizer, DFA dfa,
            int startIndex, int stopIndex, int prediction,
            ATNConfigSet configs)
    {
        System.out.println("ContextSensitivity: " + dfa.toString());
    }

}