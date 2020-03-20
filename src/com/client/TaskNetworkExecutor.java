package com.client;

import java.util.HashMap;

public class TaskNetworkExecutor extends TaskExecutor {
    private HashMap<Long, Callback<Object>> callbacks = new HashMap<>();
    private long currentId = 0;

    public <R> void execute(Task task, Callback<R> callback) {
        // send com.client.rpc

        var id = currentId++;
        callbacks.put(id, (Callback<Object>) callback);
    }

    public Object deserialize(String s) {
        return null; // deserialization
    }

    public void onResponse(String response) {
        var id = 1L; // get id
        var callback = callbacks.get(id);

        if (callback != null) {
            Object respObj = deserialize(response);

            if (respObj != null) {
                if (respObj instanceof Error) {
                    callback.onError((Error) respObj);
                } else {
                    callback.onResponse(respObj);
                }
            } else {
                callback.onError(null);
            }
        }
    }
}
