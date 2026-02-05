package com.tencent.wxcloudrun.constant;

import lombok.Getter;

/**
 * @Description 是否枚举
 * @Author zhoushuzao
 * @Date 2026/2/5 15:06
 */
@Getter
public enum YesOrNoEnum {
    /**
     * 否
     */
    NO(0, "否"),
    /**
     * 是
     */
    YES(1, "是");

    private final Integer code;
    private final String message;

    YesOrNoEnum(Integer code, String message) {
        this.code = code;
        this.message = message;
    }
}
