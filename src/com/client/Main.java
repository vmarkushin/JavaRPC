package com.client;

public class Main {
    public static void main(String[] args) {
        var client = new Client(new TaskNetworkExecutor());
        client.doSomething();
    }
}
