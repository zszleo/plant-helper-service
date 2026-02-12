package com.tencent.wxcloudrun.dto.req;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

import javax.validation.constraints.Max;
import javax.validation.constraints.Min;
import javax.validation.constraints.NotNull;

/**
 * 热力图请求参数
 * @author zszleon
 */
@Data
@Schema(description = "热力图请求参数")
public class HeatmapRequest {
    
    @NotNull(message = "年份不能为空")
    @Schema(description = "年份", example = "2026")
    private Integer year;
    
    @NotNull(message = "月份不能为空")
    @Min(value = 1, message = "月份必须在1-12之间")
    @Max(value = 12, message = "月份必须在1-12之间")
    @Schema(description = "月份（1-12）", example = "2")
    private Integer month;
}