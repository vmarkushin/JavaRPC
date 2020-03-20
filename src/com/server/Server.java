package com.server;

import com.client.Task;

import java.io.*;
import java.net.ServerSocket;

import static com.common.Common.*;

public class Server {
    private byte[] buffer = new byte[1024];
    boolean running;

    public Server() throws IOException, InterruptedException {
        var socket = new ServerSocket(10000);
        while (running) {
            var client = socket.accept();
            var in = new ObjectInputStream(client.getInputStream());
            var out = new ObjectOutputStream(client.getOutputStream());

            while (true) {
                try {
                    var task = (Task<?>) in.readObject();
                    var result = task.process();
                    out.writeObject(result);
                } catch (ClassNotFoundException e) {
                    e.printStackTrace();
                    break;
                }
//                var len = in.read();
//                var read = 0;
//                var totalRead = 0;
//                do {
//                    read = in.read(buffer, totalRead, len);
//                    totalRead += read;
//                } while (read != -1 && totalRead != len);
//                if (read == -1) {
//                    break;
//                }
                Thread.sleep(100);
            }
            client.close();
        }
        socket.close();
    }

    public static void main(String[] args) throws IOException, InterruptedException {
        var server = new Server();
//        System.out.println(mul(
//                add(
//                        num(1.0),
//                        num(3.0)
//                ),
//                num(4.0)
//        ).evaluate());
    }
}
