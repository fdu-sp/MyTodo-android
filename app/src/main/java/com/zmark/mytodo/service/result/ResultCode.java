package com.zmark.mytodo.service.result;

import androidx.annotation.NonNull;

/**
 * @author ZMARK
 * @description: 状态码
 * @date: 2023/3/5 21:40
 */
public enum ResultCode {
    /**
     * 成功
     */
    SUCCESS(200, "SUCCESS"),
    /**
     * 获取数据失败
     */
    FAIL(400, "FAIL"),
    /**
     * 没有权限
     */
    UNAUTHORIZED(401, "UNAUTHORIZED"),
    NOT_FOUND(404, "NOT_FOUND"),
    /**
     * 内部服务器错误
     */
    INTERNAL_SERVER_ERROR(500, "INTERNAL_SERVER_ERROR");
    private final int code;
    private final String codeName;

    ResultCode(int code, String codeName) {
        this.code = code;
        this.codeName = codeName;
    }

    public int getCode() {
        return code;
    }

    public String getCodeName() {
        return codeName;
    }

    @NonNull
    @Override
    public String toString() {
        return "ResultCode{" +
                "code=" + code +
                ", codeName='" + codeName + '\'' +
                '}';
    }
}
