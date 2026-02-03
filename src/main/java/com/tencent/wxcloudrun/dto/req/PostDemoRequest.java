package com.tencent.wxcloudrun.dto.req;

import lombok.Data;

import javax.validation.constraints.NotBlank;

/**
 * POST演示请求对象
 */
@Data
public class PostDemoRequest {
    
    /**
     * 内容
     */
    @NotBlank(message = "内容不能为空")
    private String content;
}