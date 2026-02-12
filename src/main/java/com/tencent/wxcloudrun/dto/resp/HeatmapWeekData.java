package com.tencent.wxcloudrun.dto.resp;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

/**
 * 热力图周数据
 * @author zszleon
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Schema(description = "热力图周数据")
public class HeatmapWeekData {
    
    @Schema(description = "周索引（0开始）", example = "0")
    private Integer weekIndex;
    
    @Schema(description = "单元格列表（7个单元格，对应一周的7天）")
    private List<HeatmapCell> cells;
}