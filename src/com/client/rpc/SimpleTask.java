package com.client.rpc;

import com.client.Task;

public class SimpleTask implements Task<Integer> {
    int sleepTime;

    public SimpleTask(int sleepTime) {
        this.sleepTime = sleepTime;
    }

    @Override
    public Integer process() {
        try {
            Thread.sleep(3000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        return 0;
    }
}
