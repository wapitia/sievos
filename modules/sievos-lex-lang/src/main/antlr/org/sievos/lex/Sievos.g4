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
grammar Sievos;

// PARSER

    /*
       @uses {
            java.lang.String
            org.sievos.kern.TI
            org.sievos.lexmodel.sp1.ExprLN
            org.sievos.lexmodel.sp1.CompositeFunctionLN
            org.sievos.lexmodel.sp1.IdentifierLN
            org.sievos.lexmodel.sp1.TBundLN
            org.sievos.lexmodel.sp1.TSingleLN
       }
        
    */    

    /*
       @rule expr {
         """ any Sievos expression, including function calls, 
             partitions, bundles. 
             """
         SP1 => expr: ExprLN
                def funcallExpr(fcall: CompositeFunctionLN)
       }
    */
expr        : fcall             # funcallExpr
            ;
    /*
       @rule fcall {
         """ a single FTFTuple or other FTFTuple stream representation 
             which can be  applied against a composite functional block
             (<tuple>) <identifier>...
    
             example: 'TFF r z'
             """
         SP1 => fcall: CompositeFunctionLN
                def funcall(part: TBundLN, fname: IdentifierLN)
                def composite(fcall: CompositeFunctionLN, fname: IdentifierLN)
       }
    */
fcall       : part fname        # funcall
            | fcall fname       # composite
            ;

    /*
       @rule part {
         """ a single Bund or other Partition representation which can be
             applied against a composite functional block 
             """
         SP1 => part: TBundLN
                def part1(bund: TBundLN)
                def partX(part: TBundLN, bund: TBundLN)
       }
    */
part        : bund              # part1
            | part PIPE bund    # partX
            ;

    /*
       @rule bund {
         """ an ordered list of tlines called a bundle
             """
         SP1 => bund: TBundLN
                def bund1(tline: TSingleLN)
                def bundX(tline: TSingleLN, bund: TBundLN)
       }
    */
bund        : tline             # bund1
// TODO: should be switched:    | bund tline       # bundX
            | tline bund        # bundX
            ;

    /*
       @rule fname {
         """ a Sievos function identifier """
         SP1 => fname: IdentifierLN
                def identifier(id: String)
       }
    */
fname       : IDENT             # identifier
            ;

    /*
       @rule tline {
         """ T or F equates to some TI value """
         SP1 => tline: TSingleLN
                def tline(ti: TI)
       }
    */
tline       : T
            | F
            ;

// LEXER

// identifiers start with lower-case
IDENT : ID_START ID_REST* ;

fragment ID_START : [a-z] ;
fragment ID_REST : [a-zA-Z0-9_$] ;

T       : 'T' ;
F       : 'F' ;
PIPE    : '|' ;

WS: [ \n\t\r]+ -> skip ;