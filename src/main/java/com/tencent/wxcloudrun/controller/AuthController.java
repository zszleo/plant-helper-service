package com.tencent.wxcloudrun.controller;

import com.tencent.wxcloudrun.context.TokenContext;
import com.tencent.wxcloudrun.dto.req.LoginRequest;
import com.tencent.wxcloudrun.dto.req.ProfileRequest;
import com.tencent.wxcloudrun.dto.resp.ApiResponse;
import com.tencent.wxcloudrun.dto.resp.LoginResponse;
import com.tencent.wxcloudrun.dto.resp.ProfileResponse;
import com.tencent.wxcloudrun.service.AuthService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import javax.validation.Valid;

/**
 * 认证控制器
 *
 * @author zszleon
 */
@Slf4j
@RestController
@RequestMapping("/api/auth")
public class AuthController {

    @Resource
    private AuthService authService;

    @Resource
    private TokenContext tokenContext;

    /**
     * 微信登录
     */
    @PostMapping("/login")
    public ApiResponse<LoginResponse> login(@Valid @RequestBody LoginRequest request) {
        LoginResponse loginResponse = authService.login(request);
        log.info("用户登录成功, openid: {}", loginResponse.getUserId());
        return ApiResponse.ok(loginResponse);
    }

    /**
     * 更新用户信息
     */
    @PostMapping("/updateProfile")
    public ApiResponse<ProfileResponse> updateProfile(@RequestHeader("X-Token") String token,
                                                      @Valid @RequestBody ProfileRequest request) {
        TokenContext.TokenInfo tokenInfo = tokenContext.validateToken(token);
        if (tokenInfo == null) {
            return ApiResponse.error("登录已过期，请重新登录");
        }
        log.info("用户更新资料, openid: {}", tokenInfo.getOpenid());
        return ApiResponse.ok(authService.updateProfile(tokenInfo.getOpenid(), request));
    }

    /**
     * 获取用户信息
     */
    @PostMapping("/getProfile")
    public ApiResponse<ProfileResponse> getProfile(@RequestHeader("X-Token") String token) {
        TokenContext.TokenInfo tokenInfo = tokenContext.validateToken(token);
        if (tokenInfo == null) {
            log.warn("获取用户信息失败, token: {}", token);
            return ApiResponse.error("登录已过期，请重新登录");
        }
        log.info("获取用户信息, openid: {}", tokenInfo.getOpenid());
        return ApiResponse.ok(authService.getProfile(tokenInfo.getOpenid()));
    }

    /**
     * 退出登录
     */
    @PostMapping("/logout")
    public ApiResponse<String> logout(@RequestHeader("X-Token") String token) {
        TokenContext.TokenInfo tokenInfo = tokenContext.validateToken(token);
        if (tokenInfo == null) {
            return ApiResponse.ok("退出成功");
        }
        authService.logout(tokenInfo.getOpenid());
        log.info("用户退出登录, openid: {}", tokenInfo.getOpenid());
        return ApiResponse.ok("退出成功");
    }
}
