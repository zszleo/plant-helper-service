package com.tencent.wxcloudrun.controller;

import com.tencent.wxcloudrun.auth.LoginUser;
import com.tencent.wxcloudrun.dto.req.ProfileRequest;
import com.tencent.wxcloudrun.dto.resp.ApiResponse;
import com.tencent.wxcloudrun.dto.resp.ProfileResponse;
import com.tencent.wxcloudrun.service.UserService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;
import javax.validation.Valid;

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
    private UserService userService;

    /**
     * 更新用户信息
     */
    @Operation(summary = "更新用户资料", description = "更新用户昵称和头像等信息")
    @PostMapping("/updateProfile")
    public ApiResponse<ProfileResponse> updateProfile(@Valid @RequestBody ProfileRequest request,
                                                      @Parameter(hidden = true) LoginUser loginUser) {
        log.info("用户更新资料, openid: {}", loginUser.getOpenid());
        return ApiResponse.ok(userService.updateProfile(loginUser.getOpenid(), request));
    }

    /**
     * 获取用户信息
     */
    @Operation(summary = "获取用户资料", description = "获取当前登录用户的资料信息")
    @PostMapping("/getProfile")
    public ApiResponse<ProfileResponse> getProfile(@Parameter(hidden = true) LoginUser loginUser) {
        log.info("获取用户信息, openid: {}", loginUser.getOpenid());
        return ApiResponse.ok(userService.getProfile(loginUser.getOpenid()));
    }
}
