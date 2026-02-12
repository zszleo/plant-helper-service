package com.tencent.wxcloudrun.dto.resp;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * 热力图单元格
 * @author zszleon
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Schema(description = "热力图单元格")
public class HeatmapCell {
    
    @Schema(description = "日期（YYYY-MM-DD格式）", example = "2026-02-01")
    private String date;
    
    @Schema(description = "日", example = "1")
    private Integer day;
    
    @Schema(description = "记录数量", example = "2")
    private Integer count;
    
    @Schema(description = "热力图层级：-1=非本月日期，0=无记录，1-4=密度层级", example = "2")
    private Integer level;
}