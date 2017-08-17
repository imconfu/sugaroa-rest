package com.sugaroa.rest;

import java.util.HashMap;
import java.util.Map;

public class Result {

    private boolean success;
    private int code;
    private String message;
    private Map<String, Object> data;

    public Result(int code, String message) {
        this.success = false;
        this.code = code;
        this.message = message;
        this.data = new HashMap<String, Object>();
    }

    public Result() {
        this.success = true;
        this.code = 1;
        this.message = "操作成功";
        this.data = new HashMap<String, Object>();
    }

    public Result(String key, Object value) {
        this.success = true;
        this.code = 1;
        this.message = "";
        this.data = new HashMap<String, Object>();
        this.data.put(key, value);
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

    public Map<String, Object> getData() {
        return this.data;
    }

    public void setData(Map<String, Object> data) {
        this.data = data;
    }

    public void setData(String key, Object value) {
        this.data.put(key, value);
    }

}
