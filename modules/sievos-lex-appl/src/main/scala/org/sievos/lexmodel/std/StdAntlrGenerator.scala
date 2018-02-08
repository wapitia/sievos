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
package org.sievos.lexmodel.std

import org.antlr.v4.runtime.CodePointCharStream
import org.antlr.v4.runtime.CommonTokenStream
import org.antlr.v4.runtime.tree.ParseTree
import org.sievos.lex.SievosLexer
import org.sievos.lex.SievosParser
import org.sievos.lexmodel.SievosLexNode
import org.sievos.lexmodel.std.antlr.SievosAntlrErrorListener
import com.wapitia.lex.antlr.AntlrGeneratorBase
import org.sievos.lexmodel.sp1.antlr.SP1AntlrVisitor
import org.sievos.lex.SievosVisitor

/**
 * An Antlr Generator for some goal in the Sievos language such as "expr" 
 *  
 * see :modules:sievos-lex-lang:src/main/antlr/org.sievos.lex.Sievos.g4
 */
abstract class StdAntlrGenerator[R](   
  sievosVisitor: SievosVisitor[SievosLexNode],
  goalOfParser: SievosParser => ParseTree, 
  finishResult: SievosLexNode => R)
  extends AntlrGeneratorBase[SievosLexNode,R,SievosParser](
    sievosVisitor, 
    goalOfParser, 
    finishResult,  
    new SievosAntlrErrorListener(), 
    (cps: CodePointCharStream) => new SievosLexer(cps),
    (cts: CommonTokenStream) => new SievosParser(cts) )
    
