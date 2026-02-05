package com.tencent.wxcloudrun.service;

import com.tencent.wxcloudrun.dto.req.LoginRequest;
import com.tencent.wxcloudrun.dto.req.ProfileRequest;
import com.tencent.wxcloudrun.dto.resp.LoginResponse;
import com.tencent.wxcloudrun.dto.resp.ProfileResponse;

/**
 * 认证服务接口
 * @author zszleon
 */
public interface AuthService {

    /**
     * 微信登录
     * @param request 登录请求
     * @return 登录响应
     */
    LoginResponse login(LoginRequest request);

    /**
     * 更新用户信息
     * @param openid 用户openid
     * @param request 更新请求
     * @return 更新后的用户信息
     */
    ProfileResponse updateProfile(String openid, ProfileRequest request);

    /**
     * 获取用户信息
     * @param openid 用户openid
     * @return 用户信息
     */
    ProfileResponse getProfile(String openid);

    /**
     * 退出登录
     * @param openid 用户openid
     */
    void logout(String openid);
}
