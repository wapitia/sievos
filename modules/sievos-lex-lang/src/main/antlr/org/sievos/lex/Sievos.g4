grammar Sievos;

// PARSER

// rule fcall
// a single FTFTuple or other FTFTuple stream representation which can be
// applied against a composite functional block
// (<tuple>) <identifier>...
//
// example: 'TFF r z'
//
fcall     : ptp func        # SINGF
          | fcall func      # MULTF
          ;

// rule ptp
// <tuple> 
// a single FTFTuple or other FTFTuple stream representation which can be
// applied against a composite functional block 
//
//ptp       : LPAR ptp RPAR  # PTUP
ptp       : tp             # TUP
          ;

// rule tp
// <T|F>...
// 
// example: 'TFT'
//          
tp        : sing           # T1
          | sing tp        # TX
          ;
          
sing      : T
          | F
          ;
          
func      : IDENT
          ;          
          
// LEXER

IDENT : ID_START ID_REST* ;

fragment ID_START : [a-z] ;
fragment ID_REST : [a-zA-Z0-9_$] ;

T       : 'T';
F       : 'F';
LPAR    : '(';
RPAR    : ')';

WS: [ \n\t\r]+ -> skip;