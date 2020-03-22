package com.common;

import java.io.Serializable;

public class Result<T extends Serializable> implements Serializable {
    private int id;
    private T value;
    private ExecutionException exception;

    private Result(int id, T value) {
        this.id = id;
        this.value = value;
    }

    public static <T extends Serializable> Result<T> ok(int id, T value) {
        return new Result<>(id, value);
    }

    public static Result<Serializable> error(int id, ExecutionException exception) {
        var result = new Result<>(id, null);
        result.exception = exception;
        return result;
    }

    public int getId() {
        return id;
    }

    public T getValue() {
        return value;
    }

    public ExecutionException getException() {
        return exception;
    }
}
