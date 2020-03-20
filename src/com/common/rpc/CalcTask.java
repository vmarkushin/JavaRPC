package com.common.rpc;

import com.common.ProcessException;
import com.common.Task;
import com.common.Common;

public class CalcTask implements Task<Double> {
    Common.Expression expr;

    public CalcTask(Common.Expression expr) {
        this.expr = expr;
    }

    @Override
    public Double process() throws ProcessException {
        return expr.evaluate();
    }
}
