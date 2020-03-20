package com.client;

import com.common.*;
import com.common.Error;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.Serializable;
import java.net.InetAddress;
import java.net.Socket;
import java.util.concurrent.ConcurrentHashMap;

public class TaskNetworkExecutor extends TaskExecutor {
    private ConcurrentHashMap<Integer, Callback<Serializable>> callbacks = new ConcurrentHashMap<>();
    private int currentId = 0;

    NetworkThread thread;

    static class NetworkThread extends Thread {
        Socket socket;
        ObjectInputStream in;
        ObjectOutputStream out;
        ConcurrentHashMap<Integer, Callback<Serializable>> callbacks;

        public NetworkThread(ConcurrentHashMap<Integer, Callback<Serializable>> callbacks) {
            super();
            this.callbacks = callbacks;
        }

        public synchronized <T extends Serializable> void send(Request<T> req) {
            try {
                out.writeObject(req);
            } catch (IOException e) {
                e.printStackTrace();
            }
        }

        @Override
        public void run() {
            super.run();

            try {
                socket = new Socket(InetAddress.getLocalHost(), 10000);
                out = new ObjectOutputStream(socket.getOutputStream());
                in = new ObjectInputStream(socket.getInputStream());
            } catch (IOException e) {
                e.printStackTrace();
            }

            while (true) {
                try {
                    Thread.sleep(100);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
                try {
                    var obj = (Result)in.readObject();
                    var callback = callbacks.get(obj.id);
                    if (callback != null) {
                        var error = obj.error;
                        if (error != null) {
                            callback.onError(error);
                        } else {
                            callback.onResponse(obj.value);
                        }
                    } else {
                        System.err.println("Unknown message id " + obj.id);
                    }
                } catch (IOException | ClassNotFoundException e) {
                    e.printStackTrace();
                    break;
                }
            }
        }
    }

    public TaskNetworkExecutor() {
        thread = new NetworkThread(callbacks);
        thread.start();
    }

    public <R extends Serializable> void execute(Task<R> task, Callback<R> callback) {
        var id = currentId++;
        callbacks.put(id, (Callback<Serializable>) callback);
        var req = new Request<>(id, task);
        thread.send(req);
    }

    public Object deserialize(String s) {
        return null; // deserialization
    }

    public void onResponse(String response) {
        var id = 1L; // get id
        var callback = callbacks.get(id);

        if (callback != null) {
            var respObj = (Serializable) deserialize(response);

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
