package com.tencent.wxcloudrun.dto.resp;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * 记录类型分布项
 * @author zszleon
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Schema(description = "记录类型分布项")
public class RecordTypeItem {
    
    @Schema(description = "记录类型", allowableValues = {"watering", "fertilizing", "growth", "photo"}, example = "watering")
    private String type;
    
    @Schema(description = "数量", example = "20")
    private Integer count;
    
    @Schema(description = "百分比", example = "44.4")
    private Double percent;
}