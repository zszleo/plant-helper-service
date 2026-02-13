package com.tencent.wxcloudrun.dto.req;

import com.fasterxml.jackson.annotation.JsonFormat;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;
import java.util.Date;

@Data
@Schema(description = "日志请求DTO")
public class LogRequestDTO {
    
    @Schema(description = "错误码", example = "E1001")
    private String code;
    
    @Schema(description = "日志级别", example = "ERROR")
    private String level;
    
    @Schema(description = "上下文信息", example = "用户登录失败")
    private String context;
    
    @Schema(description = "接口名称", example = "/api/auth/login")
    private String apiName;
    
    @Schema(description = "操作", example = "login")
    private String operation;
    
    @Schema(description = "请求参数（JSON格式）", example = "{\"openid\":\"abc123\"}")
    private String params;
    
    @Schema(description = "路径", example = "/pages/index/index")
    private String path;
    
    @Schema(description = "设备信息")
    private DeviceInfoDTO deviceInfo;
    
    @Schema(description = "日志消息", example = "网络请求超时")
    private String message;
    
    @Schema(description = "处理结果")
    private ProcessingResultDTO processingResult;
    
    @Schema(description = "来源", example = "wechat-mini-program")
    private String source;
    
    @Schema(description = "时间戳", example = "2026-02-13 08:59:01")
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+8")
    private Date timestamp;
    
    @Schema(description = "类型", example = "SYSTEM_ERROR")
    private String type;
    
    @Schema(description = "用户消息", example = "网络异常，请稍后重试")
    private String userMessage;
}