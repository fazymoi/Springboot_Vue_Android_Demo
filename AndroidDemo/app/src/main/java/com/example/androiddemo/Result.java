package com.example.androiddemo;

import lombok.Data;
import lombok.NoArgsConstructor;

@NoArgsConstructor
@Data
public class Result {
    private int code;
    private String message;
    private Object result;

    Result(int code, String message, Object data) {
        this.code = code;
        this.message = message;
        this.result = data;
    }

    public int getcode() {
        return this.code;
    }

    public String getMessage() {
        return this.message;
    }
    public Object getrst() {
        return this.result;
    }
}
