package com.tencent.wxcloudrun.dto.resp;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

/**
 * 记录类型分布响应
 * @author zszleon
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Schema(description = "记录类型分布响应")
public class RecordTypesResponse {
    
    @Schema(description = "记录总数", example = "45")
    private Integer total;
    
    @Schema(description = "类型分布项列表")
    private List<RecordTypeItem> items;
}