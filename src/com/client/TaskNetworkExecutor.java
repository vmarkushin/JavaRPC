package com.client;

import com.common.Callback;
import com.common.Request;
import com.common.Result;
import com.common.Task;

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
    private NetworkThread thread;

    public TaskNetworkExecutor() {
        thread = new NetworkThread();
        thread.start();
    }

    public <R extends Serializable> void execute(Task<R> task, Callback<R> callback) {
        var id = currentId++;
        callbacks.put(id, (Callback<Serializable>) callback);
        var req = new Request<>(id, task);
        thread.send(req);
    }

    private class NetworkThread extends Thread {
        Socket socket;
        ObjectInputStream in;
        ObjectOutputStream out;

        public NetworkThread() {
            super();
        }

        public <T extends Serializable> void send(Request<T> req) {
            try {
                out.writeObject(req);
            } catch (IOException e) {
                e.printStackTrace();
            }
        }

        void handleResponse(Result<? extends Serializable> response) {
            var callback = callbacks.get(response.getId());
            if (callback != null) {
                var error = response.getException();
                if (error != null) {
                    callback.onError(error);
                } else {
                    callback.onResponse(response.getValue());
                }
            } else {
                System.err.println("Unknown message id " + response.getId());
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
                    var response = (Result<? extends Serializable>) in.readObject();
                    handleResponse(response);
                } catch (IOException | ClassNotFoundException e) {
                    e.printStackTrace();
                    break;
                }
            }
        }
    }
}
