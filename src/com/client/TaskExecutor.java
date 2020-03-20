package com.client;

import com.common.Callback;
import com.common.Task;

import java.io.Serializable;

public abstract class TaskExecutor {
    public <R extends Serializable> void execute(Task<R> task, Callback<R> callback) {

    }
}
