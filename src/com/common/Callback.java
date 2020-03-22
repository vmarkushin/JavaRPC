package com.common;

public interface Callback<R> {
    void onResponse(R response);

    void onError(ExecutionException error);
}
