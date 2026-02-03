package com.tencent.wxcloudrun.controller;

import com.tencent.wxcloudrun.dto.req.PostDemoRequest;
import com.tencent.wxcloudrun.dto.resp.ApiResponse;
import com.tencent.wxcloudrun.exception.BusinessException;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;

/**
 * 演示控制器 - 用于测试统一异常处理器
 * @author zszleon
 */
@RestController
@RequestMapping("/api/demo")
@Validated
public class DemoController {

    /**
     * 业务异常演示
     */
    @GetMapping("/business-error")
    public ApiResponse<String> businessError() {
        throw new BusinessException(1004, "这是一个业务异常示例");
    }

    /**
     * 参数校验异常演示
     */
    @GetMapping("/validation-error")
    public ApiResponse<String> validationError(
            @NotBlank(message = "名称不能为空") @RequestParam String name,
            @NotNull(message = "年龄不能为空") @RequestParam Integer age) {
        return ApiResponse.of(0, "", "验证通过: " + name + ", " + age);
    }

    /**
     * 空指针异常演示
     */
    @GetMapping("/null-pointer")
    public ApiResponse<String> nullPointer() {
        String str = null;
        // 这里会抛出空指针异常
        int length = str.length();
        return ApiResponse.ok(String.valueOf(length));
    }

    /**
     * 非法参数异常演示
     */
    @GetMapping("/illegal-argument")
    public ApiResponse<String> illegalArgument(@RequestParam String type) {
        if (!"valid".equals(type)) {
            throw new IllegalArgumentException("参数类型不合法: " + type);
        }
        return ApiResponse.of(0, "", "参数合法");
    }

    /**
     * POST请求演示 - 用于测试请求体相关异常
     */
    @PostMapping("/post-demo")
    public ApiResponse<String> postDemo(@Valid @RequestBody PostDemoRequest request) {
        return ApiResponse.of(0, "", "收到内容: " + request.getContent());
    }

    /**
     * 非法状态异常演示
     */
    @GetMapping("/illegal-state")
    public ApiResponse<String> illegalState() {
        throw new IllegalStateException("系统处于非法状态");
    }

    /**
     * 运行时异常演示
     */
    @GetMapping("/runtime-error")
    public ApiResponse<String> runtimeError() {
        throw new RuntimeException("运行时异常示例");
    }

    /**
     * 通用异常演示
     */
    @GetMapping("/generic-error")
    public ApiResponse<String> genericError() throws Exception {
        throw new Exception("通用异常示例");
    }

    /**
     * 404路径不存在演示
     */
    @GetMapping("/not-found")
    public ApiResponse<String> notFound() {
        // 这个路径不存在，会触发404异常
        return ApiResponse.ok("正常响应");
    }
}