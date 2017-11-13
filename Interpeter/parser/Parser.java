/**
 * This file is part of the syntax parse
 *
 * In the syntax parse, we choose the recurisve decesent analysis
 * we will construct the whole program from the bottom to the top
 * that is to say contructing from the basic arithmetic expression to the whole program
 */

package parser;

import lexer.Tag;
import lexer.Token;
import lexer.Scanner;

import syntaxtree.Expr;
import logger.ErrorHandler;

public class Parser {

	private Token token; // the current token that need to parse
	private Token next;  // the next token
	private Scanner scanner; // the scanner scan the c program file and generate token

	public Parser(){
		scanner = new Scanner();
		token = null;
		next = scanner.scan(); // get the first token
		System.out.println(next);
		logger = Logger.getLogger(Scanner.class.getName());
	}

	private Expr expression(){

	}

	// && || operation
	private Expr logical(){

		Expr expr = equality();
		while(accept(Tag.AND) || accept(Tag.OR)){
			this.update();
			System.out.println("accept the token -> " + token);
			Token operator = token;
			Expr right = equality();
			expr = new Expr.Binary(expr, operator, right);
		}

		return expr;
	}

	// != , ==
	private Expr equality(){

		Expr expr = comparison();
		while(accept(Tag.NE) || accept(Tag.EQ)){
			this.update();
			System.out.println("accept the token -> " + token);
			Token operator = token;
			Expr right = comparison();
			expr = new Expr.Binary(expr, operator, right);
		}

		return expr;
	}

	// <, >, <=, >=
	private Expr comparison(){

		Expr expr = addition();
		while(accept(Tag.LT) || accept(Tag.LE) || accept(Tag.GT) || accept(Tag.GE)){
			this.update();
			System.out.println("accept the token -> " + token);
			Token operator = token;
			Expr right = addition();
			expr = new Expr.Binary(expr, operator, right);
		}

		return expr;
	}

	// +, -
	private Expr addition(){

		Expr expr = multiplication();
		while(accept(Tag.PLUS) || accept(Tag.MINUS))){
			this.update();
			System.out.println("accept the token -> " + token);
			Token operator = token;
			Expr right = multiplication();
			expr = new Expr.Binary(expr, operator, right);
		}

		return expr;
	}

	// *, /
	private Expr multiplication(){

		Expr expr = unary();
		while(accept(Tag.MUL) || accept(Tag.SLASH)){
			this.update();
			System.out.println("accept the token -> " + token);
			Token operator = token;
			Expr right = unary();
			expr = new Expr.Binary(expr, operator, right);
		}

		return expr;
	}
	
	// !, -
	private Expr unary(){

		if(accept(Tag.NOT) || accept(Tag.MINUS)){
			this.update();
			System.out.println("accept the token -> " + token);
			Token operator = token;
			Expr right = primary();
			return new Expr.Unary(operator, right);
		}

		return primary();
	}

	// the basic element
	private Expr primary(){

		if(accept(Tag.NUM) || accept(Tag.REAL) || accept(Tag.CHARACTER) || accept(Tag.STRING)){
			this.update();
			System.out.println("accept the token -> " + token);
			return new Expr.Literal(token.literal);
		}

		if(accept(Tag.LPAREN)){
			Expr expr = expression();
			expect(Tag.RPAREN);
			return new Expr.Grouping(expr);
		}

		ErrorHandler.error(next.line, "Invalid expression");
		System.exit(1);
	}

	public void update(){
		token = next;
		next = scanner.scan();
	}

	/**
	 * the recursive decesent method must look forward a token, it can determine the next step
	 * so this function is that judge whether the next token match the expected token
	 * if match then return true, else return false
	 * @param  symbol the expected Tag
	 * @return        boolean
	 */
	public boolean accept(Tag symbol){
		if(next != null && next.type.equals(symbol)){ // if it accept the current token
			return true;
		}
		return false;
	}

	/**
	 * just for expecting some match symbol like ), ], }
	 * if it expect failed, then throw an error
	 * @param symbol the expected Tag
	 */
	public void expect(Tag symbol){
		if(!accept(symbol)) error("Missing the symbol <" + symbol.toString() + ">.");
	}

	/**
	 * using the logging module to print the error when parse c program
	 * @param the error message
	 */
	public void error(String message){
		logger.severe(message);
	}
}