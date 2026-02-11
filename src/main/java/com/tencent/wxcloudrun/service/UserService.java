package com.tencent.wxcloudrun.service;

import com.tencent.wxcloudrun.dto.req.ProfileRequest;
import com.tencent.wxcloudrun.dto.resp.ProfileResponse;

/**
 * 用户接口
 * @author zszleon
 */
public interface UserService {

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

}
