/**
 * This file is part of lexcial parser
 *
 * A token tag distribute to distinguish different type of token
 * It includes five categories : identifier, keyword, literal, separator, operator
 * literal includes : constant and literal string
 */

package lexer;

public enum Tag {
	/** identifier */
	ID,
	/** keywords */
	INT, CHAR, FLOAT, DOUBLE, LONG, VOID, DO, WHILE, IF, ELSE, SWITCH, CASE, DEFAULT, BREAK, CONTINUE, STATIC, STRUCT, RETURN,
	/** literal */
	NUM, REAL, CHARACTER, STRING,
	/** sepatator */
	LPAREN, RPAREN, LBRACE, RBRACE, LBRACKET, RBRACKET, SEMICOLON, COMMA,
	/** operator */
	ASSIGN, LT, GT, EQ, LE, GE, NE, PLUS, MINUS, MUL, SLASH, PLUSASSIGN, MINUSASSIGN, MULASSIGN, SLASHASSGIN, AND, OR, DOUBLEPLUS, DOUBLEMINUS,
	NOT, BITAND, BITOR, BITXOR, MOD
}

/** 
 * the operators inlcude the following components:
 * = , < , > , ==, >=, <=, !=, +, -, *, /, +=, -=, *=, /=, &&, ||, ++, --, !, &, |, ^, %
 */