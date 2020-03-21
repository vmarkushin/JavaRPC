package com.client;

import java.io.IOException;

import static com.common.Calc.*;

public class Main {
    public static void main(String[] args) throws IOException, InterruptedException {
        var client = new Client(new TaskNetworkExecutor());
        Thread.sleep(100);
        client.sleep();
        client.calculate(mul(
                add(
                        num(1.0),
                        num(3.0)
                ),
                num(4.0)
        ));
        client.calculate(div(num(1.0), num(0.0)));
        Thread.sleep(1000);
    }
}
