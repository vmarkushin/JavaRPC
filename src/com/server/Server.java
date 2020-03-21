package com.server;

import com.common.ExecutionException;
import com.common.Task;
import com.common.Request;
import com.common.Result;

import java.io.*;
import java.net.ServerSocket;

public class Server {
    boolean running = true;
    ServerSocket socket;
    ObjectInputStream in;
    ObjectOutputStream out;

    public Server() throws IOException {
        socket = new ServerSocket(10000);
    }

    void handleRequest(Request<Task<Serializable>> request) throws IOException {
        System.out.println("New request: " + request);
        var id = request.id;
        try {
            var result = request.value.execute();
            System.out.println("Result: " + result);
            out.writeObject(Result.ok(id, result));
        } catch (ExecutionException e) {
            String text = e.getMessage();
            System.err.println("Error: " + text);
            out.writeObject(Result.error(id, text));
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

    public static void main(String[] args) throws IOException {
        var server = new Server();
        server.run();
    }
}
