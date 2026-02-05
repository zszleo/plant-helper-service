package com.tencent.wxcloudrun.auth;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;
import java.io.Serializable;

/**
 * 当前登录用户信息
 * 轻量级设计，仅包含认证必需字段
 * @author zszleon
 */
@Data
@Schema(hidden = true)
public class LoginUser implements Serializable {
    /** 微信openid（业务用户标识） */
    private String openid;
    
    /** 用户表主键ID（Long类型，仅用于关联用户表） */
    private Long userId;
    
    public LoginUser(String openid, Long userId) {
        this.openid = openid;
        this.userId = userId;
    }
    
    public LoginUser() {}
}