package com.common.rpc;

import com.common.ExecutionException;
import com.common.Task;
import com.common.Calc;

public class CalcTask implements Task<Double> {
    Calc.Expression expr;

    public CalcTask(Calc.Expression expr) {
        this.expr = expr;
    }

    @Override
    public Double execute() throws ExecutionException {
        return expr.evaluate();
    }
}
