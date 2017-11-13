/**
 * The file is the core of lexcial parser
 *
 * The Scanner class uses an c programming file as input stream
 * and tokenizer the stream into different token according to the automata
 *
 * when a lexeme is splited, then according to the lexeme type construct different token
 * and record the tag of the lexeme and the line number of the position, and the value if it has
 */
package lexer;

import java.io.IOException;
import java.io.FileNotFoundException;
import java.io.BufferedReader;
import java.io.FileReader;

import java.util.logging.Logger;

import utils.Transform;
import logger.ErrorHandler;

public class Scanner {

	public static final String PATH = "../test/test.c"; // the tokenizer file path

	private int pos = 0; // record the current position the scanner recongize
	private int line = 1; // record the line number of the token

	private String stream;

	public Scanner(){
		this.clean();
		this.stream = read(PATH);
	}

	public static String read(String path){

		String content = null; // the content in the c programming file

		try {
			BufferedReader reader = new BufferedReader(new FileReader(path));

			StringBuilder builder = new StringBuilder();
			String line = reader.readLine();

			while (line != null){
				builder.append(line);
				builder.append(System.lineSeparator()); // add a '\n'
				line = reader.readLine();
			}

			content = builder.toString();
			reader.close(); // close the io stream

		}catch(FileNotFoundException ex){
			ex.printStackTrace();
		}catch(IOException e){
			e.printStackTrace();
		}

		/** if successfully read the c file, then return the file content, else return null */
		return content;
	}

	/**
	 *  a util function that clean all records
	 *  clean the position and set the pos to 0, set the line to 1
	 */
	private void clean(){
		this.pos = 0;
		this.line = 1;
	}

	public Token scan(){

		if(pos >= stream.length()) return null; // if the stream finished tokenizer then just return null

		char ch = this.transform(stream.charAt(pos));
		switch(ch){
			case 'a': return recognize_word();
			case 'd': return recognize_digit();
			case 'w': pos++; return scan();
			case '\'': return recognize_char();
			case '\"': return recognize_string();
			case '=': pos++; if(next('=')) return new Token(Tag.EQ, "==", null, line); else return new Token(Tag.ASSIGN, "=", null, line);
			case '<': pos++; if(next('=')) return new Token(Tag.LE, "<=", null, line); else return new Token(Tag.LT, "<", null, line);
			case '>': pos++; if(next('=')) return new Token(Tag.GT, ">=", null, line); else return new Token(Tag.GE, ">", null, line);
			case '!': pos++; if(next('=')) return new Token(Tag.NE, "!=", null, line); else return new Token(Tag.NOT, "!", null, line);
			case '+':
				pos++; // fetch the next char
				if(next('=')) return new Token(Tag.PLUSASSIGN, "+=", null, line);
				else if(next('+')) return new Token(Tag.DOUBLEPLUS, "++", null, line); else return new Token(Tag.PLUS, "+", null, line);
			case '-':
				pos++; // fetch the next char
				if(next('=')) return new Token(Tag.MINUSASSIGN, "-=", null, line);
				else if(next('-')) return new Token(Tag.DOUBLEMINUS, "--", null, line); else return new Token(Tag.MINUS, "-", null, line);
			case '/': 
				pos++; // fetch the next char
				if (next('=')) return new Token(Tag.SLASHASSGIN, "/=", null, line);
				else if(next('/')) { this.recognize_line_comment(); return this.scan(); }
				else if(next('*')) { this.recognize_multiply_comment(); return this.scan(); } 
				else return new Token(Tag.SLASH, "/", null, line);
			case '*': pos++; if(next('=')) return new Token(Tag.MULASSIGN, "*=", null, line); else return new Token(Tag.MUL, "*", null, line);
			case '&': pos++; if(next('&')) return new Token(Tag.AND, "&&", null, line); else return new Token(Tag.BITAND, "&", null, line);
			case '|': pos++; if(next('|')) return new Token(Tag.OR, "||", null, line); else return new Token(Tag.BITOR, "|", null, line);
			case '^': pos++; return new Token(Tag.BITXOR, "^", null, line);
			case '%': pos++; return new Token(Tag.MOD, "%", null, line);
			case '(': pos++; return new Token(Tag.LPAREN, "(", null, line);
			case ')': pos++; return new Token(Tag.RPAREN, ")", null, line);
			case '{': pos++; return new Token(Tag.LBRACE, "{", null, line);
			case '}': pos++; return new Token(Tag.RBRACE, "}", null, line);
			case '[': pos++; return new Token(Tag.LBRACKET, "[", null, line);
			case ']': pos++; return new Token(Tag.RBRACKET, "]", null, line);
			case ';': pos++; return new Token(Tag.SEMICOLON, ";", null, line);
			case ',': pos++; return new Token(Tag.COMMA, ",", null, line);
			default:
				return null;
		}
	}

	/**
	 * a utility function that handles the keywords or identifier
	 * if it is a keyword just return the base Token else return the word with Tag.ID
	 * @return Token
	 */
	private Token recognize_word(){
		String lexeme = Character.toString(stream.charAt(pos)); // add current character to lexeme
		pos++; // get the next char

		// add more character to the lexeme as much as possible
		while(pos < stream.length() && isValidChar(stream.charAt(pos))){
			lexeme += stream.charAt(pos);
			pos++; // fetch the next char
		}

		Tag tag = Transform.transform(lexeme);

		if(tag == null){ // if it is an identifier, then genertation a word token
			return new Token(Tag.ID, lexeme, lexeme, line);
		}

		// if it is a keyword, just return the tag
		return new Token(tag, lexeme, lexeme, line);
	}

	/**
	 * a utility function that handles the integer and real
	 * if it is an integer we must convert it to int type
	 * if it is a real or scientific representation, we must convert it to double type
	 * @return Num or Real
	 */
	private Token recognize_digit(){
		boolean isInteger = true; // record the lexeme whether is integer or not

		String lexeme = Character.toString(stream.charAt(pos)); // add current character to lexeme
		pos++; // get the next char

		// reconginze more character as much as possible
		while(pos < stream.length() && Character.isDigit(stream.charAt(pos))){
			lexeme += stream.charAt(pos);
			pos++; // fetch the next char
		}

		if(pos < stream.length() && '.' == stream.charAt(pos)) {
			lexeme += '.'; // if encouter a float point the it must be a real number
			pos++; // fetch the next char
			isInteger = false; // the lexeme is a real number

			while(pos < stream.length() && Character.isDigit(stream.charAt(pos))){
				lexeme += stream.charAt(pos);
				pos++; // fetch the next char
			}
		}

		if(pos < stream.length() && 'E' == stream.charAt(pos)){
			lexeme += 'E'; // scientific representation
			pos++; // fetch the next char
			if(pos < stream.length() && ('+' == stream.charAt(pos) || '-' == stream.charAt(pos))){ // if afer the E is '+' or '-' then add it to the lexeme
				lexeme += stream.charAt(pos);
				pos++;
			}

			while(pos < stream.length() && Character.isDigit(stream.charAt(pos))){
				lexeme += stream.charAt(pos);
				pos++; // fetch the next char
			}
		}

		if(isInteger)
			return new Token(Tag.NUM, lexeme, Integer.valueOf(lexeme), line);
		else
			return new Token(Tag.REAL, lexeme, Double.parseDouble(lexeme), line);
	}

	/**
	 * a utility function that handles the literal character, and it should handle some error when multiply character in the quote
	 * @return Character token
	 */
	private Token recognize_char(){
		pos++; // fetch the next charcter, ignore the character '
		if(pos < stream.length()){
			char ch = stream.charAt(pos);
			pos++; // fetch the next character

			if('\\' == ch && pos < stream.length()){
				switch(stream.charAt(pos)){
					case 'n':
						pos++; ch = '\n'; break;
					case 't':
						pos++; ch = '\t'; break;
					case 'r':
						pos++; ch = '\r'; break;
					case '\\':
						pos++; ch = '\\'; break;
					case '\'':
						pos++; ch = '\''; break;
					case '\"':
						pos++; ch = '\"'; break;
					case '\0':
						pos++; ch = '\0'; break;
					default:
						break; 
				}
			}

			boolean error = false;

			while(pos < stream.length() && '\'' != stream.charAt(pos)){// judge the next charter is character '
				pos++; // just ignore the following character
				error = true;
			}
			pos++; // ignore the right quote character '
			if(error) ErrorHandler.error(line, "Multiple character in char type error ");
			return new Token(Tag.CHARACTER, Character.toString(ch), new Character(ch), line);
		}

		ErrorHandler.error(line, "Unexpected character");
		return null;
	}

	/**
	 * a utility function that handles the literal string
	 * @return return word with tag string
	 */
	private Token recognize_string(){
		pos++; // fetch the next character, ignore the double character "
		if(pos < stream.length()){
			String lexeme = Character.toString(stream.charAt(pos));
			pos++; // fetch the next character
			while(pos < stream.length() && '\"' != stream.charAt(pos)){
				lexeme += stream.charAt(pos); // add every character in the quote to the lexeme
				pos++;
			}

			pos++; // ignore the right double quote "
			return new Token(Tag.STRING, lexeme, lexeme, line);
		}
		ErrorHandler.error(line, "Unexpected String");
		return null;
	}

	/**
	 * a utility function that ignore the line comment
	 * ignore all character until encouter a '\n'
	 */
	private void recognize_line_comment(){
		while(pos < stream.length() && '\n' != stream.charAt(pos)){
			pos++;
		}

		this.line ++; // increase the line number
		pos++; // ignore the '\n'
	}

	/**
	 * a utility function that recongize the multiply line comment
	 * ingore all character even if it is '\n'
	 * it ends until it meets "*"
	 */
	private void recognize_multiply_comment(){
		while(pos < stream.length()){
			if('*' == stream.charAt(pos)){
				pos++; // fectch the next character
				if(next('/')) break;
			}else if('\n' == stream.charAt(pos)){
				pos++; // ignore the '\n'
				this.line ++; // increase the line number
			}
			else{
				pos++; // just ingore all character
			}
		}

		pos++; // ignore the '/' character 
	}

	/**
	 * a utility function that judge wether the ch is a valid indentifier character
	 * @param  ch the current character that recognizes
	 * @return    if it is letter or digit or underscore then return true else false
	 */
	public boolean isValidChar(char ch){
		if(Character.isLetter(ch) || Character.isDigit(ch) || ch == '_')
			return true;
		else
			return false;
	}

	/**
	 * a utility function that tranform the current character into a more reconizable character
	 * @param  ch the current character that recongnize
	 * @return   letter -> 'a'
	 *           digit -> 'd'
	 *           whitespace -> 'w'
	 */
	public char transform(char ch){
		if(Character.isLetter(ch))
			return 'a';
		else if(Character.isDigit(ch))
			return 'd';
		else if(Character.isWhitespace(ch)){
			if(ch == '\n' ) line ++;
			return 'w';
		}else
			return ch;
	}

	/**
	 * a utility function that handle if the next character match the given character
	 * @param  ch the expected next character
	 * @return    boolean
	 */
	public boolean next(char ch){
		if(pos < stream.length() && ch == stream.charAt(pos)){
			pos++; // fetch the next character
			return true; // ch match the next character
		}else{
			return false;
		}
	}
}
