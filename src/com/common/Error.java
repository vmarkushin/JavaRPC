package com.common;

import java.io.Serializable;

public class Error implements Serializable {
    String text;

    public Error(String text) {
        this.text = text;
    }

    @Override
    public String toString() {
        return "Error{" +
                "text='" + text + '\'' +
                '}';
    }
}
