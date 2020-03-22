package com.common;

public class Calc {
    public static Expression num(double n) {
        return new Num(n);
    }

    public static Expression add(Expression expr0, Expression expr1) {
        return new Add(expr0, expr1);
    }

    public static Expression sub(Expression expr0, Expression expr1) {
        return new Sub(expr0, expr1);
    }

    public static Expression div(Expression expr0, Expression expr1) {
        return new Div(expr0, expr1);
    }

    public static Expression mul(Expression expr0, Expression expr1) {
        return new Mul(expr0, expr1);
    }

    public static Expression parse(String expr) throws ExecutionException {
        return new Parser(expr).parse();
    }

    public interface Expression {
        double evaluate() throws ExecutionException;
    }

    public static class Num implements Expression {
        double n;

        public Num(double n) {
            this.n = n;
        }

        @Override
        public double evaluate() {
            return n;
        }
    }

    public static class UnaryMinus implements Expression {
        Expression e;

        public UnaryMinus(Expression e) {
            this.e = e;
        }

        @Override
        public double evaluate() throws ExecutionException {
            return -e.evaluate();
        }
    }

    public static abstract class BinaryExpr implements Expression {
        Expression expr0;
        Expression expr1;

        public BinaryExpr(Expression expr0, Expression expr1) {
            this.expr0 = expr0;
            this.expr1 = expr1;
        }
    }

    public static class Add extends BinaryExpr {
        public Add(Expression expr0, Expression expr1) {
            super(expr0, expr1);
        }

        @Override
        public double evaluate() throws ExecutionException {
            return expr0.evaluate() + expr1.evaluate();
        }
    }

    public static class Sub extends BinaryExpr {
        public Sub(Expression expr0, Expression expr1) {
            super(expr0, expr1);
        }

        @Override
        public double evaluate() throws ExecutionException {
            return expr0.evaluate() - expr1.evaluate();
        }
    }

    public static class Mul extends BinaryExpr {
        public Mul(Expression expr0, Expression expr1) {
            super(expr0, expr1);
        }

        @Override
        public double evaluate() throws ExecutionException {
            return expr0.evaluate() * expr1.evaluate();
        }
    }

    public static class Div extends BinaryExpr {
        public Div(Expression expr0, Expression expr1) {
            super(expr0, expr1);
        }

        @Override
        public double evaluate() throws ExecutionException {
            double denominator = expr1.evaluate();
            if (denominator == 0.0) {
                throw new ExecutionException("Division by 0");
            }
            return expr0.evaluate() / denominator;
        }
    }

    private static class Parser {
        int pos = -1, ch;
        String expr;

        public Parser(String expr) {
            this.expr = expr;
        }

        void nextChar() {
            ch = (++pos < expr.length()) ? expr.charAt(pos) : -1;
        }

        boolean eat(int charToEat) {
            while (ch == ' ') nextChar();
            if (ch == charToEat) {
                nextChar();
                return true;
            }
            return false;
        }

        Expression parse() throws ExecutionException {
            nextChar();
            Expression x = parseExpr();
            if (pos < expr.length()) throw new ExecutionException("unexpected: " + (char) ch);
            return x;
        }

        Expression parseExpr() throws ExecutionException {
            Expression x = parseMul();
            while (true) {
                if (eat('+')) x = new Add(x, parseMul());
                else if (eat('-')) x = new Sub(x, parseMul());
                else return x;
            }
        }

        Expression parseMul() throws ExecutionException {
            Expression x = parsePrimary();
            while (true) {
                if (eat('*')) x = new Mul(x, parsePrimary());
                else if (eat('/')) x = new Div(x, parsePrimary());
                else return x;
            }
        }

        Expression parsePrimary() throws ExecutionException {
            if (eat('+')) return parsePrimary();
            if (eat('-')) return new UnaryMinus(parsePrimary());

            Expression x;
            int startPos = this.pos;
            if (eat('(')) {
                x = parseExpr();
                eat(')');
            } else if ((ch >= '0' && ch <= '9') || ch == '.') {
                while ((ch >= '0' && ch <= '9') || ch == '.') nextChar();
                x = new Num(Double.parseDouble(expr.substring(startPos, this.pos)));
            } else {
                throw new ExecutionException("unexpected: " + (char) ch);
            }

            return x;
        }
    }
}
