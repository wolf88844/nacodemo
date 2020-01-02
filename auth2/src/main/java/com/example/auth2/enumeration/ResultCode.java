package com.example.auth2.enumeration;

public enum ResultCode {

    SUCCESS(200,"Successful"),
    FAILED(500,"Failed"),
    NOT_LOGIN(401,"not login"),
    ACCESS_DENIED(403,"accoutn not actived"),
    DB_ERROR(503,"Error querying database"),
    PARAM_PARAMETER_ERROR(501,"Parameter error"),
    PRARM_PARAMETER_IS_NULL(502,"Parameter is null"),
    ;

    private int code;
    private String message;

    ResultCode(int code,String message){
        this.code = code;
        this.message = message;
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

    public String getMessage(String message){
        return String.format(this.message,message==null?"":message);
    }

    public void setMessage(String message) {
        this.message = message;
    }
}
