package com.client;

public interface Callback<R> {
    void onResponse(R response);
    void onError(Error error);
}
