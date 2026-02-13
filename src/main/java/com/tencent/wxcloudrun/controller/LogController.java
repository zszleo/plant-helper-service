package com.tencent.wxcloudrun.controller;

import com.tencent.wxcloudrun.dto.req.LogRequestDTO;
import com.tencent.wxcloudrun.dto.resp.ApiResponse;
import com.tencent.wxcloudrun.service.LogService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;
import javax.validation.Valid;

@Tag(name = "日志管理", description = "小程序日志收集接口")
@Slf4j
@RestController
@RequestMapping("/api/logs")
public class LogController {

    @Resource
    private LogService logService;

    @Operation(summary = "创建日志", description = "接收小程序上报的日志信息")
    @PostMapping("/create")
    public ApiResponse<String> createLog(@Valid @RequestBody LogRequestDTO request) {
        log.info("收到日志上报请求, code: {}, level: {}, type: {}", 
                request.getCode(), request.getLevel(), request.getType());
        
        boolean success = logService.createLog(request);
        if (success) {
            return ApiResponse.ok("日志创建成功");
        } else {
            return ApiResponse.error("日志创建失败");
        }
    }
}