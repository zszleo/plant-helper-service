package com.tencent.wxcloudrun.auth;

/**
 * 认证常量类
 * @author zszleon
 */
public class AuthConstant {
    public static final String TOKEN_HEADER = "X-Token";
    public static final String OPENID_ATTR = "openid";
    public static final String USER_ID_ATTR = "userId";
    
    public static final String[] EXCLUDE_PATHS = {
        "/api/auth/login",
        "/error"
    };
}