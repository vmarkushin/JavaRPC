package com.common;

import java.io.Serializable;

public interface Task<R extends Serializable> extends Serializable {
    R execute() throws ExecutionException;
}
