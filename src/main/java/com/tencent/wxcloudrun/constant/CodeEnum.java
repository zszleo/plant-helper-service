package com.tencent.wxcloudrun.constant;

/**
 * 响应码枚举
 * @author zszleon
 */
public enum CodeEnum {
    /**
     * 0
     */
    SUCCESS(0),
    /**
     * 1
     */
    ERROR(1);

    private final Integer code;

    CodeEnum(Integer code){
        this.code = code;
    }

    public Integer getCode() {
        return code;
    }
}