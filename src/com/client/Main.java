package com.client;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;

public class Main {
    public static void main(String[] args) throws IOException, InterruptedException {
        var client = new Client(new TaskNetworkExecutor());
        Thread.sleep(200); // wait for connection
        client.sleep();
        client.calculate("(1+12)-14*5");
        client.calculate("1/0");
        var reader = new BufferedReader(new InputStreamReader(System.in));
        String line;
        while ((line = reader.readLine()) != null) {
            client.calculate(line);
        }
    }
}
