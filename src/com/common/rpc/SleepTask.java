package com.common.rpc;

import com.common.Task;

import java.io.Serializable;

public class SleepTask implements Task<Serializable> {
    int sleepTime;

    public SleepTask(int sleepTime) {
        this.sleepTime = sleepTime;
    }

    @Override
    public Serializable process() {
        System.out.println("Sleeping for " + sleepTime);
        try {
            Thread.sleep(sleepTime);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        return null;
    }
}
