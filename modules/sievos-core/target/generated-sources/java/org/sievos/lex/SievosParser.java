// Generated from org\sievos\lex\Sievos.g4 by ANTLR 4.7
package org.sievos.lex;
import org.antlr.v4.runtime.atn.*;
import org.antlr.v4.runtime.dfa.DFA;
import org.antlr.v4.runtime.*;
import org.antlr.v4.runtime.misc.*;
import org.antlr.v4.runtime.tree.*;
import java.util.List;
import java.util.Iterator;
import java.util.ArrayList;

@SuppressWarnings({"all", "warnings", "unchecked", "unused", "cast"})
public class SievosParser extends Parser {
	static { RuntimeMetaData.checkVersion("4.7", RuntimeMetaData.VERSION); }

	protected static final DFA[] _decisionToDFA;
	protected static final PredictionContextCache _sharedContextCache =
		new PredictionContextCache();
	public static final int
		T=1, F=2, LPAR=3, RPAR=4;
	public static final int
		RULE_t1 = 0, RULE_t2 = 1, RULE_t3 = 2;
	public static final String[] ruleNames = {
		"t1", "t2", "t3"
	};

	private static final String[] _LITERAL_NAMES = {
		null, "'T'", "'F'", "'('", "')'"
	};
	private static final String[] _SYMBOLIC_NAMES = {
		null, "T", "F", "LPAR", "RPAR"
	};
	public static final Vocabulary VOCABULARY = new VocabularyImpl(_LITERAL_NAMES, _SYMBOLIC_NAMES);

	/**
	 * @deprecated Use {@link #VOCABULARY} instead.
	 */
	@Deprecated
	public static final String[] tokenNames;
	static {
		tokenNames = new String[_SYMBOLIC_NAMES.length];
		for (int i = 0; i < tokenNames.length; i++) {
			tokenNames[i] = VOCABULARY.getLiteralName(i);
			if (tokenNames[i] == null) {
				tokenNames[i] = VOCABULARY.getSymbolicName(i);
			}

			if (tokenNames[i] == null) {
				tokenNames[i] = "<INVALID>";
			}
		}
	}

	@Override
	@Deprecated
	public String[] getTokenNames() {
		return tokenNames;
	}

	@Override

	public Vocabulary getVocabulary() {
		return VOCABULARY;
	}

	@Override
	public String getGrammarFileName() { return "Sievos.g4"; }

	@Override
	public String[] getRuleNames() { return ruleNames; }

	@Override
	public String getSerializedATN() { return _serializedATN; }

	@Override
	public ATN getATN() { return _ATN; }

	public SievosParser(TokenStream input) {
		super(input);
		_interp = new ParserATNSimulator(this,_ATN,_decisionToDFA,_sharedContextCache);
	}
	public static class T1Context extends ParserRuleContext {
		public TerminalNode T() { return getToken(SievosParser.T, 0); }
		public TerminalNode F() { return getToken(SievosParser.F, 0); }
		public T1Context(ParserRuleContext parent, int invokingState) {
			super(parent, invokingState);
		}
		@Override public int getRuleIndex() { return RULE_t1; }
		@Override
		public void enterRule(ParseTreeListener listener) {
			if ( listener instanceof SievosListener ) ((SievosListener)listener).enterT1(this);
		}
		@Override
		public void exitRule(ParseTreeListener listener) {
			if ( listener instanceof SievosListener ) ((SievosListener)listener).exitT1(this);
		}
		@Override
		public <T> T accept(ParseTreeVisitor<? extends T> visitor) {
			if ( visitor instanceof SievosVisitor ) return ((SievosVisitor<? extends T>)visitor).visitT1(this);
			else return visitor.visitChildren(this);
		}
	}

	public final T1Context t1() throws RecognitionException {
		T1Context _localctx = new T1Context(_ctx, getState());
		enterRule(_localctx, 0, RULE_t1);
		int _la;
		try {
			enterOuterAlt(_localctx, 1);
			{
			setState(6);
			_la = _input.LA(1);
			if ( !(_la==T || _la==F) ) {
			_errHandler.recoverInline(this);
			}
			else {
				if ( _input.LA(1)==Token.EOF ) matchedEOF = true;
				_errHandler.reportMatch(this);
				consume();
			}
			}
		}
		catch (RecognitionException re) {
			_localctx.exception = re;
			_errHandler.reportError(this, re);
			_errHandler.recover(this, re);
		}
		finally {
			exitRule();
		}
		return _localctx;
	}

	public static class T2Context extends ParserRuleContext {
		public T2Context(ParserRuleContext parent, int invokingState) {
			super(parent, invokingState);
		}
		@Override public int getRuleIndex() { return RULE_t2; }
	 
		public T2Context() { }
		public void copyFrom(T2Context ctx) {
			super.copyFrom(ctx);
		}
	}
	public static class TUP2Context extends T2Context {
		public T1Context v0;
		public T1Context v1;
		public List<T1Context> t1() {
			return getRuleContexts(T1Context.class);
		}
		public T1Context t1(int i) {
			return getRuleContext(T1Context.class,i);
		}
		public TUP2Context(T2Context ctx) { copyFrom(ctx); }
		@Override
		public void enterRule(ParseTreeListener listener) {
			if ( listener instanceof SievosListener ) ((SievosListener)listener).enterTUP2(this);
		}
		@Override
		public void exitRule(ParseTreeListener listener) {
			if ( listener instanceof SievosListener ) ((SievosListener)listener).exitTUP2(this);
		}
		@Override
		public <T> T accept(ParseTreeVisitor<? extends T> visitor) {
			if ( visitor instanceof SievosVisitor ) return ((SievosVisitor<? extends T>)visitor).visitTUP2(this);
			else return visitor.visitChildren(this);
		}
	}

	public final T2Context t2() throws RecognitionException {
		T2Context _localctx = new T2Context(_ctx, getState());
		enterRule(_localctx, 2, RULE_t2);
		try {
			_localctx = new TUP2Context(_localctx);
			enterOuterAlt(_localctx, 1);
			{
			setState(8);
			((TUP2Context)_localctx).v0 = t1();
			setState(9);
			((TUP2Context)_localctx).v1 = t1();
			}
		}
		catch (RecognitionException re) {
			_localctx.exception = re;
			_errHandler.reportError(this, re);
			_errHandler.recover(this, re);
		}
		finally {
			exitRule();
		}
		return _localctx;
	}

	public static class T3Context extends ParserRuleContext {
		public T3Context(ParserRuleContext parent, int invokingState) {
			super(parent, invokingState);
		}
		@Override public int getRuleIndex() { return RULE_t3; }
	 
		public T3Context() { }
		public void copyFrom(T3Context ctx) {
			super.copyFrom(ctx);
		}
	}
	public static class TUP3Context extends T3Context {
		public T1Context v1;
		public T2Context v2;
		public T1Context t1() {
			return getRuleContext(T1Context.class,0);
		}
		public T2Context t2() {
			return getRuleContext(T2Context.class,0);
		}
		public TUP3Context(T3Context ctx) { copyFrom(ctx); }
		@Override
		public void enterRule(ParseTreeListener listener) {
			if ( listener instanceof SievosListener ) ((SievosListener)listener).enterTUP3(this);
		}
		@Override
		public void exitRule(ParseTreeListener listener) {
			if ( listener instanceof SievosListener ) ((SievosListener)listener).exitTUP3(this);
		}
		@Override
		public <T> T accept(ParseTreeVisitor<? extends T> visitor) {
			if ( visitor instanceof SievosVisitor ) return ((SievosVisitor<? extends T>)visitor).visitTUP3(this);
			else return visitor.visitChildren(this);
		}
	}

	public final T3Context t3() throws RecognitionException {
		T3Context _localctx = new T3Context(_ctx, getState());
		enterRule(_localctx, 4, RULE_t3);
		try {
			_localctx = new TUP3Context(_localctx);
			enterOuterAlt(_localctx, 1);
			{
			setState(11);
			((TUP3Context)_localctx).v1 = t1();
			setState(12);
			((TUP3Context)_localctx).v2 = t2();
			}
		}
		catch (RecognitionException re) {
			_localctx.exception = re;
			_errHandler.reportError(this, re);
			_errHandler.recover(this, re);
		}
		finally {
			exitRule();
		}
		return _localctx;
	}

	public static final String _serializedATN =
		"\3\u608b\ua72a\u8133\ub9ed\u417c\u3be7\u7786\u5964\3\6\21\4\2\t\2\4\3"+
		"\t\3\4\4\t\4\3\2\3\2\3\3\3\3\3\3\3\4\3\4\3\4\3\4\2\2\5\2\4\6\2\3\3\2\3"+
		"\4\2\r\2\b\3\2\2\2\4\n\3\2\2\2\6\r\3\2\2\2\b\t\t\2\2\2\t\3\3\2\2\2\n\13"+
		"\5\2\2\2\13\f\5\2\2\2\f\5\3\2\2\2\r\16\5\2\2\2\16\17\5\4\3\2\17\7\3\2"+
		"\2\2\2";
	public static final ATN _ATN =
		new ATNDeserializer().deserialize(_serializedATN.toCharArray());
	static {
		_decisionToDFA = new DFA[_ATN.getNumberOfDecisions()];
		for (int i = 0; i < _ATN.getNumberOfDecisions(); i++) {
			_decisionToDFA[i] = new DFA(_ATN.getDecisionState(i), i);
		}
	}
}