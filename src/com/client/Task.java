package com.client;

import java.io.Serializable;

public interface Task<R extends Serializable> extends Serializable {
    R process();
}
