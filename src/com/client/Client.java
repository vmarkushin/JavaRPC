package com.client;

import com.common.Callback;
import com.common.Error;
import com.common.rpc.CalcTask;
import com.common.rpc.SleepTask;
import com.common.Common;

import java.io.Serializable;

public class Client {
    TaskExecutor executor;

    public Client(TaskExecutor executor) {
        this.executor = executor;
    }

    void sleep() {
        var task = new SleepTask(1000);

        executor.execute(task, new Callback<Serializable>() {
            @Override
            public void onResponse(Serializable response) {
            }

            @Override
            public void onError(Error error) {
                System.out.println("Error: " + error);
            }
        });
    }

    void calculate(Common.Expression expr) {
        var task = new CalcTask(expr);

        executor.execute(task, new Callback<Double>() {
            @Override
            public void onResponse(Double response) {
                System.out.println("Result: " + response);
            }

            @Override
            public void onError(Error error) {
                System.out.println("Error: " + error);
            }
        });
    }
}
