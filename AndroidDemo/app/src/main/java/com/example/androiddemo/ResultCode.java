package com.example.androiddemo;

public class ResultCode {
    public static final int SUCCESS = 200;
    public static final int FAIL = 400;
    public static final int UNAUTHORIZED=401;
    public static final int NOT_FOUND=(404);

    public static final int INTERNAL_SERVER_ERROR=(500);
    public int code;

    ResultCode(int code) {
        this.code = code;
    }
}
