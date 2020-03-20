package com.common;

import java.io.Serializable;

public class Request<T extends Serializable> implements Serializable {
    public int id;
    public T value;

    public Request(int id, T value) {
        this.id = id;
        this.value = value;
    }
}
