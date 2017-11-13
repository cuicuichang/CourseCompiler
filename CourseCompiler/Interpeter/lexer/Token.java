
package lexer;

public class Token {
	public final Tag type;
	public final String lexeme;
	public final Object literal;
	public final int line;

	public Token(Tag type, String lexeme, Object literal, int line){
		this.type = type;
		this.lexeme = lexeme;
		this.literal = literal;
		this.line = line;
	}

	@Override
	public String toString(){
		return type + " " + lexeme + " " + literal;
	}
}