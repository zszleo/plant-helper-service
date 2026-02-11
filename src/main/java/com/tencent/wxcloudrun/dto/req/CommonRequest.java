package com.tencent.wxcloudrun.dto.req;

import com.fasterxml.jackson.annotation.JsonFormat;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

import javax.validation.constraints.NotBlank;

/**
 * @Description 统一请求参数类
 * @Author zhoushuzao
 * @Date 2026/2/5 11:37
 */
@Data
@Schema(description = "统一请求参数类")
public class CommonRequest {

    /**
     * 业务id
     */
    @Data
    @Schema(description = "ID请求参数")
    public static class Id {
        @Schema(description = "业务ID", example = "123456789", type = "string")
        @NotBlank(message = "id不能为空")
        @JsonFormat(shape = JsonFormat.Shape.STRING)
        private Long id;
    }

    /**
     * 启用-1/停用-0
     */
    @Data
    @Schema(description = "启用停用请求参数")
    public static class IsEnabled {
        @Schema(description = "业务ID", example = "123456789", type = "string")
        @NotBlank(message = "id不能为空")
        @JsonFormat(shape = JsonFormat.Shape.STRING)
        private Long id;
        
        @Schema(description = "是否启用（1-启用，0-停用）", allowableValues = {"0", "1"}, example = "1")
        @NotBlank(message = "是否启用不能为空")
        private Integer isEnabled;
    }
}
