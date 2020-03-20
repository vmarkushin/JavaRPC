package com.client;

import com.client.rpc.SimpleTask;

public class Client {
    TaskExecutor executor;

    public Client(TaskExecutor executor) {
        this.executor = executor;
    }

    void doSomething() {
        var task = new SimpleTask(1000);

        executor.execute(task, new Callback<Long>() {
            @Override
            public void onResponse(Long response) {
                System.out.println("Response "+ response);
            }

            @Override
            public void onError(Error error) {
                System.out.println("Error: "+ error);
            }
        });
    }
}
