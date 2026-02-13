package com.tencent.wxcloudrun.dto.req;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

@Data
@Schema(description = "设备信息")
public class DeviceInfoDTO {
    
    @Schema(description = "语言", example = "zh_CN")
    private String language;
    
    @Schema(description = "设备型号", example = "iPhone 13")
    private String model;
    
    @Schema(description = "平台", example = "ios")
    private String platform;
    
    @Schema(description = "屏幕高度", example = "844")
    private String screenHeight;
    
    @Schema(description = "屏幕宽度", example = "390")
    private String screenWidth;
    
    @Schema(description = "系统版本", example = "iOS 15.0")
    private String system;
    
    @Schema(description = "小程序版本", example = "2.0.1")
    private String version;
}