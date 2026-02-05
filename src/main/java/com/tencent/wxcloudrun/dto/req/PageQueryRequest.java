package com.tencent.wxcloudrun.dto.req;

import lombok.Data;
import javax.validation.constraints.Min;
import javax.validation.constraints.NotBlank;

@Data
public class PageQueryRequest {

    @Min(value = 1, message = "页码必须大于0")
    private Integer pageNum = 1;

    @Min(value = 1, message = "每页条数必须大于0")
    private Integer pageSize = 10;

    @NotBlank(message = "userId不能为空")
    private String userId;
}
