package com.common.rpc;

import com.common.Calc;
import com.common.ExecutionException;
import com.common.Task;

public class CalcTask implements Task<Double> {
    String expr;

    public CalcTask(String expr) {
        this.expr = expr;
    }

    @Override
    public Double execute() throws ExecutionException {
        return Calc.parse(expr).evaluate();
    }
}
