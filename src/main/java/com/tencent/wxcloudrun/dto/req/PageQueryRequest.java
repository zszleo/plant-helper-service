package com.tencent.wxcloudrun.dto.req;

import io.swagger.v3.oas.annotations.Hidden;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;
import javax.validation.constraints.Min;


/**
 * 分页查询请求基类
 * @author zszleon
 */
@Data
@Schema(description = "分页查询请求基类")
public class PageQueryRequest {

    @Schema(description = "页码", example = "1")
    @Min(value = 1, message = "页码必须大于0")
    private Integer pageNum = 1;

    @Schema(description = "每页条数", example = "10")
    @Min(value = 1, message = "每页条数必须大于0")
    private Integer pageSize = 10;

    @Hidden()
    private Long userId;
}
