package com.server;

import com.common.ProcessException;
import com.common.Task;
import com.common.Request;
import com.common.Result;

import java.io.*;
import java.net.ServerSocket;

public class Server {
    boolean running = true;

    public Server() throws InterruptedException, IOException {
        var socket = new ServerSocket(10000);
        while (running) {
            try {
                var client = socket.accept();
                System.out.println("Client connected");
                var in = new ObjectInputStream(client.getInputStream());
                var out = new ObjectOutputStream(client.getOutputStream());

                while (true) {
                    try {
                        var req = (Request<Task<?>>) in.readObject();
                        System.out.println("New request: " + req);
                        var id = req.id;
                        try {
                            var result = req.value.process();
                            System.out.println("Result: " + result);
                            out.writeObject(new Result<>(id, result));
                        } catch (ProcessException e) {
                            String text = e.getMessage();
                            System.err.println("Error: " + text);
                            out.writeObject(Result.error(id, text));
                        }
                    } catch (ClassNotFoundException e) {
                        e.printStackTrace();
                        break;
                    }
                    Thread.sleep(100);
                }
                client.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        socket.close();
    }

    public static void main(String[] args) throws IOException, InterruptedException {
        var server = new Server();
    }
}
