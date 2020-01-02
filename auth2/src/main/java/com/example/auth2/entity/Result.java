package com.example.auth2.entity;

import java.util.Date;

/**
 * @ClassName Result
 * @Author LIUHANPENG
 * @Date 2020/1/2 0002 17:19
 **/
public class Result {

    private int code;
    private String message;
    private Object data;
    private Date timestamp;

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

    public Object getData() {
        return data;
    }

    public void setData(Object data) {
        this.data = data;
    }

    public Date getTimestamp() {
        return timestamp;
    }

    public void setTimestamp(Date timestamp) {
        this.timestamp = timestamp;
    }
}
