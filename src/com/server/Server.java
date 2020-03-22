package com.server;

import com.common.ExecutionException;
import com.common.Request;
import com.common.Result;
import com.common.Task;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.Serializable;
import java.net.ServerSocket;

public class Server {
    boolean running = true;
    ServerSocket socket;
    ObjectInputStream in;
    ObjectOutputStream out;

    public Server() throws IOException {
        socket = new ServerSocket(10000);
    }

    public static void main(String[] args) throws IOException {
        var server = new Server();
        server.run();
    }

    void handleRequest(Request<Task<Serializable>> request) throws IOException {
        System.out.println("New request: " + request);
        var id = request.id;
        try {
            var result = request.value.execute();
            System.out.println("Result: " + result);
            out.writeObject(Result.ok(id, result));
        } catch (ExecutionException e) {
            System.err.println("Error: " + e.getMessage());
            out.writeObject(Result.error(id, e));
        }
    }

    public void run() throws IOException {
        while (running) {
            try {
                var client = socket.accept();
                System.out.println("Client connected");

                in = new ObjectInputStream(client.getInputStream());
                out = new ObjectOutputStream(client.getOutputStream());

                while (true) {
                    try {
                        var req = (Request<Task<Serializable>>) in.readObject();
                        handleRequest(req);
                    } catch (ClassNotFoundException e) {
                        e.printStackTrace();
                        break;
                    }
                    Thread.sleep(100);
                }
                client.close();
            } catch (IOException | InterruptedException e) {
                e.printStackTrace();
            }
        }
        socket.close();
    }
}
