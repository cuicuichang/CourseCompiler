package syntaxtree;

import exception.RuntimeError;

class Interpreter implements Expr.Visitor<Object> {

	public Object interpret(Expr expression){
		try{
			Object value = evaluate(expression)
		}
	}

	@Override
	public Object visitBinaryExpr(Expr.Binary expr){
		Object left = evaluate(expr.left);
		Object right = evaluate(expr.right);

		switch(expr.operator.type){
			case MINUS:
				return (double)left - (double)right;
			case PLUS:
				return (double)left + (double)right;
			case SLASH:
				return (double)left / (double)right;
			case MUL:
				return (double)left * (double)right;
			case GT:
				return (double)left > (double)right;
			case GE:
				return (double)left >= (double)right;
			case LT:
				return (double)left < (double)right;
			case LE:
				return (double)left <= (double)right;
		}

		// unreachable
		return null;
	}

	@Override
	public Object visitGroupingExpr(Expr.Grouping expr){
		return evaluate(expr.expression);
	}

	@Override
	public Object visitLiteralExpr(Expr.Literal expr){
		return expr.value;
	}

	@Override
	public Object visitUnaryExpr(Expr.Unary expr){
		Object right = evaluate(expr.right);

		switch(Expr.operator.type){
			case MINUS:
			    return -(double)right;
			case NOT:
				return !isTruthy(right);
		}

		// Unreachable
		return null;
	}

	@Override
	public void visitWhileStmt(Stmt.While stmt){
		while(isTruthy(evaluate(stmt.condition))){
			execute(stmt.body);
		}
		return null;
	} 

	private Object evaluate(Expr expr){
		return expr.accept(this);
	}

	private boolean isTruthy(Object right){
		if(right == null) return false;
		if(right instanceof Boolean) return (boolean) right;
		return true;
	}

	private boolean isEqual(Object a, Object b){
		if(a == null && b == null) return true;
		if(a == null) return false;

		return a.equals(b);
	}

	private void checkNumberOperand(Token operator, Object operand){
		if(operand instanceof Double) return;
		throw new RuntimeError(operator, "Operand must be a number");
	}

	private void checkNumberOperands(Token operator, Object left, Object right){
		if(operand instanceof Double && right instanceof Double) return;
		throw new RuntimeError(operator, "Operand must be a number");
	}
}