package com.tencent.wxcloudrun.service.impl;

import lombok.extern.slf4j.Slf4j;

import com.tencent.wxcloudrun.config.WxConfig;
import com.tencent.wxcloudrun.dao.UserMapper;
import com.tencent.wxcloudrun.dto.req.ProfileRequest;
import com.tencent.wxcloudrun.dto.resp.ProfileResponse;
import com.tencent.wxcloudrun.exception.BusinessException;
import com.tencent.wxcloudrun.model.User;
import com.tencent.wxcloudrun.service.UserService;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;

/**
 * 认证服务实现类
 * @author zszleon
 */
@Slf4j
@Service
public class UserServiceImpl implements UserService {
    @Resource
    private UserMapper userMapper;


    @Override
    public ProfileResponse updateProfile(String openid, ProfileRequest request) {
        User user = userMapper.findByOpenid(openid);
        if (user == null) {
            throw new BusinessException("用户不存在");
        }

        if (request.getNickname() != null) {
            user.setNickname(request.getNickname());
        }
        if (request.getAvatarUrl() != null) {
            user.setAvatarUrl(request.getAvatarUrl());
        }

        userMapper.updateById(user);

        return new ProfileResponse(openid,user.getOpenid(),user.getNickname(),user.getAvatarUrl(),user.getCreateTime());
    }

    @Override
    public ProfileResponse getProfile(String openid) {
        User user = userMapper.findByOpenid(openid);
        if (user == null) {
            throw new BusinessException("用户不存在");
        }

        return new ProfileResponse(openid,user.getOpenid(),user.getNickname(),user.getAvatarUrl(),user.getCreateTime());
    }
}
