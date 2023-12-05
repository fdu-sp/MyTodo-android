package com.zmark.mytodo.api.result;

public class Result<T> {
    private Integer code;
    private String msg;
    private T object;

    public Result() {
    }

    public Result(Integer code, String msg, T object) {
        this.code = code;
        this.msg = msg;
        this.object = object;
    }

    public Integer getCode() {
        return code;
    }

    public void setCode(Integer code) {
        this.code = code;
    }

    public String getMsg() {
        return msg;
    }

    public void setMsg(String msg) {
        this.msg = msg;
    }

    public T getObject() {
        return object;
    }

    public void setObject(T object) {
        this.object = object;
    }
}
