package com.tencent.wxcloudrun.constant;

/**
 * HTTP请求头常量类
 * @author zszleon
 */
public class HeaderConstant {

    /**
     * X-Forwarded-For - 获取客户端真实IP
     */
    public static final String X_FORWARDED_FOR = "x-forwarded-for";

    /**
     * X-Wx-Local-Debug - 微信本地调试标识
     */
    public static final String X_WX_LOCAL_DEBUG = "x-wx-local-debug";

    /**
     * X-Wx-Cloudbase-Access-Token - 微信云托管访问令牌
     */
    public static final String X_WX_CLOUDBASE_ACCESS_TOKEN = "x-wx-cloudbase-access-token";

    /**
     * X-Wx-Openid - 微信用户OpenID
     */
    public static final String X_WX_OPENID = "x-wx-openid";

    /**
     * X-Wx-Unionid - 微信用户UnionID
     */
    public static final String X_WX_UNIONID = "x-wx-unionid";

    /**
     * X-Wx-Appid - 微信小程序AppID
     */
    public static final String X_WX_APPID = "x-wx-appid";

    /**
     * Connection - 连接类型
     */
    public static final String CONNECTION = "connection";

    /**
     * Host - 主机地址
     */
    public static final String HOST = "host";

    /**
     * Accept-Encoding - 接受的编码方式
     */
    public static final String ACCEPT_ENCODING = "accept-encoding";

    /**
     * Content-Length - 内容长度
     */
    public static final String CONTENT_LENGTH = "content-length";

    /**
     * X-Wx-Container-Path - 微信容器路径
     */
    public static final String X_WX_CONTAINER_PATH = "x-wx-container-path";

    /**
     * X-Wx-Env - 微信环境标识
     */
    public static final String X_WX_ENV = "x-wx-env";

    /**
     * Referer - 来源页面
     */
    public static final String REFERER = "referer";

    /**
     * Content-Type - 内容类型
     */
    public static final String CONTENT_TYPE = "content-type";

    /**
     * X-Token - 自定义令牌
     */
    public static final String X_TOKEN = "x-token";

    /**
     * X-Wx-Service - 微信服务标识
     */
    public static final String X_WX_SERVICE = "x-wx-service";

    /**
     * User-Agent - 用户代理
     */
    public static final String USER_AGENT = "user-agent";
}
