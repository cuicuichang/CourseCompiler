
package test;

import lexer.Token;
import lexer.Tag;
import syntaxtree.Expr;
import syntaxtree.AstPrinter;

public class AstPrinterTest {

	public static void main(String[] args){

		/**
		 *  -123 + (45.67)
		 */
		Expr expression = new Expr.Binary(
			new Expr.Unary(
				new Token(Tag.MINUS, "-", null, 1),
				new Expr.Literal(123)),
			new Token(Tag.MUL, "*", null, 1),
			new Expr.Grouping(
				new Expr.Literal(45.67)));


		System.out.println(new AstPrinter().print(expression));
	}
}