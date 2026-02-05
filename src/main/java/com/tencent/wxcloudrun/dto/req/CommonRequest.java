package com.tencent.wxcloudrun.dto.req;


import lombok.Data;

import javax.validation.constraints.NotBlank;

/**
 * @Description 统一请求参数类
 * @Author zhoushuzao
 * @Date 2026/2/5 11:37
 */
@Data
public class CommonRequest {

    @Data
    public static class Id {
        @NotBlank(message = "id不能为空")
        private Long id;
    }
}
