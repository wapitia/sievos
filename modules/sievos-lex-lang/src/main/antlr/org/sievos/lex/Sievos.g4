grammar Sievos;

// PARSER

fcall     : t3 func         # SINGF
          | fcall func      # MULTF
          ;

t1        : T
          | F
          ;
          
t2        : v0=t1 v1=t1      # TUP2 
          ;
          
t3exp     : LPAR t3exp RPAR  # SINGT3
          | t3               # ALONET3
          ;
          
t3        : v1=t1 v2=t2      # TUP3
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