package com.common;

import java.io.Serializable;

public class Common {
    public interface Expression extends Serializable {
        double evaluate() throws ProcessException;
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

    public static abstract class BinaryExpr<E0 extends Expression, E1 extends Expression> implements Expression {
        E0 expr0;
        E1 expr1;

        public BinaryExpr(E0 expr0, E1 expr1) {
            this.expr0 = expr0;
            this.expr1 = expr1;
        }
    }

    public static class Add<E0 extends Expression, E1 extends Expression> extends BinaryExpr<E0, E1> {
        public Add(E0 expr0, E1 expr1) {
            super(expr0, expr1);
        }

        @Override
        public double evaluate() throws ProcessException {
            return expr0.evaluate() + expr1.evaluate();
        }
    }

    public static class Sub<E0 extends Expression, E1 extends Expression> extends BinaryExpr<E0, E1> {
        public Sub(E0 expr0, E1 expr1) {
            super(expr0, expr1);
        }

        @Override
        public double evaluate() throws ProcessException {
            return expr0.evaluate() - expr1.evaluate();
        }
    }

    public static class Mul<E0 extends Expression, E1 extends Expression> extends BinaryExpr<E0, E1> {
        public Mul(E0 expr0, E1 expr1) {
            super(expr0, expr1);
        }

        @Override
        public double evaluate() throws ProcessException {
            return expr0.evaluate() * expr1.evaluate();
        }
    }

    public static class Div<E0 extends Expression, E1 extends Expression> extends BinaryExpr<E0, E1> {
        public Div(E0 expr0, E1 expr1) {
            super(expr0, expr1);
        }

        @Override
        public double evaluate() throws ProcessException {
            double denominator = expr1.evaluate();
            if (denominator == 0.0) {
                throw new ProcessException("Division by 0");
            }
            return expr0.evaluate() / denominator;
        }
    }

    public static Expression num(double n) {
        return new Num(n);
    }

    public static <E0 extends Expression, E1 extends Expression> Expression add(E0 expr0, E1 expr1) {
        return new Add<>(expr0, expr1);
    }

    public static <E0 extends Expression, E1 extends Expression> Expression sub(E0 expr0, E1 expr1) {
        return new Sub<>(expr0, expr1);
    }

    public static <E0 extends Expression, E1 extends Expression> Expression div(E0 expr0, E1 expr1) {
        return new Div<>(expr0, expr1);
    }

    public static <E0 extends Expression, E1 extends Expression> Expression mul(E0 expr0, E1 expr1) {
        return new Mul<>(expr0, expr1);
    }
}
