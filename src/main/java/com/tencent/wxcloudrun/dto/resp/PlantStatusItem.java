package com.tencent.wxcloudrun.dto.resp;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * 植物状态分布项
 * @author zszleon
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Schema(description = "植物状态分布项")
public class PlantStatusItem {
    
    @Schema(description = "状态值", allowableValues = {"healthy", "growing", "need-care", "diseased"}, example = "healthy")
    private String status;
    
    @Schema(description = "数量", example = "8")
    private Integer count;
    
    @Schema(description = "百分比", example = "53.3")
    private Double percent;
}