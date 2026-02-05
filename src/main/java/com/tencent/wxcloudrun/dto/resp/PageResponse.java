package com.tencent.wxcloudrun.dto.resp;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Builder;
import lombok.Data;
import java.util.List;

@Schema(description = "分页响应")
@Data
@Builder
public class PageResponse<T> {
    @Schema(description = "数据列表")
    private List<T> list;
    
    @Schema(description = "总记录数", example = "100")
    private Long total;
    
    @Schema(description = "当前页码", example = "1")
    private Integer pageNum;
    
    @Schema(description = "每页条数", example = "10")
    private Integer pageSize;
    
    @Schema(description = "总页数", example = "10")
    private Integer totalPages;
}
