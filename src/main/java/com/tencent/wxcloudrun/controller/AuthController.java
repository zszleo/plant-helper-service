package com.tencent.wxcloudrun.controller;

import com.tencent.wxcloudrun.auth.LoginUser;
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

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.tags.Tag;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.responses.ApiResponses;

/**
 * 认证控制器
 *
 * @author zszleon
 */
@Tag(name = "认证管理", description = "用户登录、资料管理、退出登录等接口")
@Slf4j
@RestController
@RequestMapping("/api/auth")
public class AuthController {

    @Resource
    private AuthService authService;

    /**
     * 微信登录
     */
    @Operation(summary = "微信登录", description = "通过微信code获取用户openid并生成登录token")
    @PostMapping("/login")
    public ApiResponse<LoginResponse> login(@Valid @RequestBody LoginRequest request) {
        log.info("用户请求登录, code: {}", request.getCode());
        LoginResponse loginResponse = authService.login(request);

        return ApiResponse.ok(loginResponse);
    }

    /**
     * 更新用户信息
     */
    @Operation(summary = "更新用户资料", description = "更新用户昵称和头像等信息，需要Token认证")
    @SecurityRequirement(name = "TokenAuth")
    @PostMapping("/updateProfile")
    public ApiResponse<ProfileResponse> updateProfile(@Valid @RequestBody ProfileRequest request,
                                                      @Parameter(hidden = true) LoginUser loginUser) {
        log.info("用户更新资料, openid: {}", loginUser.getOpenid());
        return ApiResponse.ok(authService.updateProfile(loginUser.getOpenid(), request));
    }

    /**
     * 获取用户信息
     */
    @Operation(summary = "获取用户资料", description = "获取当前登录用户的资料信息，需要Token认证")
    @SecurityRequirement(name = "TokenAuth")
    @PostMapping("/getProfile")
    public ApiResponse<ProfileResponse> getProfile(@Parameter(hidden = true) LoginUser loginUser) {
        log.info("获取用户信息, openid: {}", loginUser.getOpenid());
        return ApiResponse.ok(authService.getProfile(loginUser.getOpenid()));
    }

    /**
     * 退出登录
     */
    @Operation(summary = "退出登录", description = "清除当前用户的登录Token，需要Token认证")
    @SecurityRequirement(name = "TokenAuth")
    @PostMapping("/logout")
    public ApiResponse<String> logout(@Parameter(hidden = true) LoginUser loginUser) {
        log.info("用户退出登录, openid: {}", loginUser.getOpenid());
        authService.logout(loginUser.getOpenid());
        return ApiResponse.ok("退出成功");
    }
}
