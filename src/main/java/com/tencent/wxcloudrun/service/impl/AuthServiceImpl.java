package com.tencent.wxcloudrun.service.impl;

import cn.hutool.http.HttpUtil;
import cn.hutool.json.JSONObject;
import cn.hutool.json.JSONUtil;
import lombok.extern.slf4j.Slf4j;

import com.tencent.wxcloudrun.config.WxConfig;
import com.tencent.wxcloudrun.context.TokenContext;
import com.tencent.wxcloudrun.dao.UserMapper;
import com.tencent.wxcloudrun.dto.req.LoginRequest;
import com.tencent.wxcloudrun.dto.req.ProfileRequest;
import com.tencent.wxcloudrun.dto.resp.LoginResponse;
import com.tencent.wxcloudrun.dto.resp.ProfileResponse;
import com.tencent.wxcloudrun.exception.BusinessException;
import com.tencent.wxcloudrun.model.User;
import com.tencent.wxcloudrun.service.AuthService;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.HashMap;
import java.util.Map;

/**
 * 认证服务实现类
 * @author zszleon
 */
@Slf4j
@Service
public class AuthServiceImpl implements AuthService {

    private static final String JSCODE2SESSION_URL = "http://api.weixin.qq.com/sns/jscode2session";

    @Resource
    private WxConfig wxConfig;

    @Resource
    private UserMapper userMapper;

    @Resource
    private TokenContext tokenContext;
    @Value("${spring.profiles.active:dev}")
    private String env;

    @Override
    public LoginResponse login(LoginRequest request) {
        Map<String, String> sessionInfo = getSessionInfo(request.getCode());
        if (sessionInfo == null) {
            throw new BusinessException("微信登录失败：无法获取用户信息");
        }

        String openid = sessionInfo.get("openid");

        User user = userMapper.findByOpenid(openid);
        boolean isNewUser = false;

        if (user == null) {
            user = new User();
            user.setOpenid(openid);
            user.setNickname("微信用户");
            userMapper.insert(user);
            isNewUser = true;
        }

        String token = tokenContext.createToken(openid, user.getId());

        return new LoginResponse(token,isNewUser);
    }

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

    @Override
    public void logout(String openid) {
        tokenContext.removeTokenByOpenid(openid);
    }

     private Map<String, String> getSessionInfo(String code) {
         if (!"prod".equals(env)) {
             Map<String, String> sessionInfo = new HashMap<>();
             sessionInfo.put("openid", "testUser");
             sessionInfo.put("session_key", "nnasdjfasdmfafsdfas");
             return sessionInfo;
         }
        String url = String.format("%s?appid=%s&secret=%s&js_code=%s&grant_type=authorization_code",
                JSCODE2SESSION_URL, wxConfig.getAppid(), wxConfig.getSecret(), code);

        try {
            log.info(url);
            String responseBody = HttpUtil.get(url);
            JSONObject result = JSONUtil.parseObj(responseBody);
            log.info("请求响内容：{}",result.toString());
            if (result.containsKey("errcode") && result.getInt("errcode") != 0) {
                return null;
            }

            Map<String, String> sessionInfo = new HashMap<>();
            sessionInfo.put("openid", result.getStr("openid"));
            sessionInfo.put("session_key", result.getStr("session_key"));
            return sessionInfo;
        } catch (Exception e) {
            log.error("",e);
            return null;
        }
    }
}
