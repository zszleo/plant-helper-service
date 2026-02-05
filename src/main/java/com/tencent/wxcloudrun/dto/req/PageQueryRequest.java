package com.tencent.wxcloudrun.dto.req;

import io.swagger.v3.oas.annotations.Hidden;
import lombok.Data;
import javax.validation.constraints.Min;


/**
 * 分页查询请求基类
 * @author zszleon
 */
@Data
public class PageQueryRequest {

    @Min(value = 1, message = "页码必须大于0")
    private Integer pageNum = 1;

    @Min(value = 1, message = "每页条数必须大于0")
    private Integer pageSize = 10;

    @Hidden()
    private Long userId;
}
