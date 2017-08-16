package com.sugaroa.rest;

public class Result {

    private boolean success;
    private int code;
    private String message;

    public Result(int code, String message) {
        this.success = false;
        this.code = code;
        this.message = message;
    }

    public Result(boolean success) {
        this.success = success;
        this.code = 1;
        this.message = "";
    }
    public boolean getSuccess() {
        return success;
    }

    public void setSuccess(boolean success) {
        this.success = success;
    }

    public int getCode() {
        return code;
    }

    public void setCode(int code) {
        this.code = code;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }
}
