package com.common;

import java.io.Serializable;

public class Result<T extends Serializable> implements Serializable {
    public int id;
    public T value;
    public Error error;

    public Result(int id, T value) {
        this.id = id;
        this.value = value;
    }

    public static Result<Serializable> error(int id, String text) {
        var result = new Result<Serializable>(id, null);
        result.error = new Error(text);
        return result;
    }
}
