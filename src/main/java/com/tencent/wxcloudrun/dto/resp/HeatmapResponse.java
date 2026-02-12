package com.tencent.wxcloudrun.dto.resp;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

/**
 * 热力图响应
 * @author zszleon
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Schema(description = "热力图响应")
public class HeatmapResponse {
    
    @Schema(description = "年份", example = "2026")
    private Integer year;
    
    @Schema(description = "月份（1-12）", example = "2")
    private Integer month;
    
    @Schema(description = "总记录数", example = "45")
    private Integer totalRecords;
    
    @Schema(description = "日历数据（按周分组）")
    private List<HeatmapWeekData> calendarData;
}