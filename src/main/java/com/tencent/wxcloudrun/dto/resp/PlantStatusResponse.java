package com.tencent.wxcloudrun.dto.resp;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

/**
 * 植物状态分布响应
 * @author zszleon
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Schema(description = "植物状态分布响应")
public class PlantStatusResponse {
    
    @Schema(description = "植物总数", example = "15")
    private Integer total;
    
    @Schema(description = "状态分布项列表")
    private List<PlantStatusItem> items;
}