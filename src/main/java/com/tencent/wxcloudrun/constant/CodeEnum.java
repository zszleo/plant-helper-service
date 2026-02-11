package com.tencent.wxcloudrun.constant;

import lombok.Getter;

/**
 * 响应码枚举
 * @author zszleon
 */
@Getter
public enum CodeEnum {
    /**
     * 成功
     */
    SUCCESS(200, "操作成功"),
    /**
     * 系统错误
     */
    ERROR(-1, "系统错误"),
    /**
     * 参数错误
     */
    PARAM_ERROR(400, "参数错误"),
    /**
     * 未授权
     */
    UNAUTHORIZED(401, "未授权"),
    /**
     * 禁止访问
     */
    FORBIDDEN(403, "禁止访问"),
    /**
     * 资源不存在
     */
    NOT_FOUND(404, "资源不存在"),
    /**
     * 方法不允许
     */
    METHOD_NOT_ALLOWED(405, "方法不允许"),
    /**
     * 请求超时
     */
    REQUEST_TIMEOUT(408, "请求超时"),
    /**
     * 请求实体过大
     */
    PAYLOAD_TOO_LARGE(413, "请求实体过大"),
    /**
     * 请求的URI过长
     */
    URI_TOO_LONG(414, "请求的URI过长"),
    /**
     * 不支持的媒体类型
     */
    UNSUPPORTED_MEDIA_TYPE(415, "不支持的媒体类型"),
    /**
     * 请求范围不满足
     */
    RANGE_NOT_SATISFIABLE(416, "请求范围不满足"),
    /**
     * 内部服务器错误
     */
    INTERNAL_SERVER_ERROR(500, "内部服务器错误"),
    /**
     * 服务不可用
     */
    SERVICE_UNAVAILABLE(503, "服务不可用"),
    /**
     * 网关超时
     */
    GATEWAY_TIMEOUT(504, "网关超时"),
    /**
     * 数据库错误
     */
    DATABASE_ERROR(1001, "数据库错误"),
    /**
     * 数据不存在
     */
    DATA_NOT_FOUND(1002, "数据不存在"),
    /**
     * 数据已存在
     */
    DATA_ALREADY_EXISTS(1003, "数据已存在"),
    /**
     * 操作失败
     */
    OPERATION_FAILED(1004, "操作失败");

    private final Integer code;
    private final String message;

    CodeEnum(Integer code, String message){
        this.code = code;
        this.message = message;
    }
}