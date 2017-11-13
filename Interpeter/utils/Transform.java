/**
 * This file is part of the utils class
 *
 * when a token is recongized,
 * if it it a keyword, then we should convert it's coorspanding tag
 * else it is a identifier, we just reverse the same like it.
 */

package utils;
import lexer.Tag;

public class Transform {

	public static Tag transform(String lexeme){
		switch(lexeme){
			case "int":
				return Tag.INT;
			case "char":
				return Tag.CHAR;
			case "float":
				return Tag.FLOAT;
			case "double":
				return Tag.DOUBLE;
			case "long":
				return Tag.VOID;
			case "do":
				return Tag.DO;
			case "while":
				return Tag.WHILE;
			case "if":
				return Tag.IF;
			case "else":
				return Tag.ELSE;
			case "switch":
				return Tag.SWITCH;
			case "case":
				return Tag.CASE;
			case "default":
				return Tag.DEFAULT;
			case "break":
				return Tag.BREAK;
			case "continue":
				return Tag.CONTINUE;
			case "static":
				return Tag.STATIC;
			case "struct":
				return Tag.STRUCT;
			case "return":
				return Tag.RETURN;
			default:
				return null;
		}
	}
}