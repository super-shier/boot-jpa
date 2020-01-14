package com.shier.common.boot.jpa.common.enums;

import java.util.Objects;

/**
 * @Author: liyunbiao
 * @Date: 2019/7/24 4:24 PM
 * @description
 */
public enum ErrorCode {
    SYSTEM_ERROR("4001", "系统错误"),
    INPUT_DATA_ERROR("4002", "信息输入有误");

    private String code;
    private String msg;

    ErrorCode(String code, String msg) {
        this.code = code;
        this.msg = msg;
    }

    public String getCode() {
        return code;
    }

    public String getMsg() {
        return msg;
    }

    public static ErrorCode find(String code) {
        for (ErrorCode errorCode : values()) {
            if (Objects.equals(errorCode.code, code)) {
                return errorCode;
            }
        }
        return null;
    }
}
