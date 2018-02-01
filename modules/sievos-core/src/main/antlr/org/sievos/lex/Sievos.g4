grammar Sievos;

// PARSER

t1        : T
          | F
          ;
          
t2        : v0=t1 v1=t1    # TUP2 
          ;
          
          
t3        : v1=t1 v2=t2    # TUP3
          ;
          
// LEXER

fragment IDENT : ID_CHAR+ ;

fragment ID_CHAR : [0-9] ;

T       : 'T';
F       : 'F';
LPAR    : '(';
RPAR    : ')';
