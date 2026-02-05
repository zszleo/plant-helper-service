package com.tencent.wxcloudrun.dto.req;


import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Data;

import javax.validation.constraints.NotBlank;

/**
 * @Description 统一请求参数类
 * @Author zhoushuzao
 * @Date 2026/2/5 11:37
 */
@Data
public class CommonRequest {

    /**
     * 业务id
     */
    @Data
    public static class Id {
        @NotBlank(message = "id不能为空")
        @JsonFormat(shape = JsonFormat.Shape.STRING)
        private Long id;
    }

    /**
     * 启用-1/停用-0
     */
    @Data
    public static class IsEnabled {
        @NotBlank(message = "id不能为空")
        @JsonFormat(shape = JsonFormat.Shape.STRING)
        private Long id;
        @NotBlank(message = "是否启用不能为空")
        private Integer isEnabled;
    }
}
