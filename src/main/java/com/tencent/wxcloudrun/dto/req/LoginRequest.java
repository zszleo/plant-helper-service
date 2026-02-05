package com.tencent.wxcloudrun.dto.req;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;
import javax.validation.constraints.NotBlank;

/**
 * 登录请求
 * @author zszleon
 */
@Data
@Schema(description = "登录请求")
public class LoginRequest {
    @Schema(description = "微信登录凭证code", example = "023A45B0")
    @NotBlank(message = "code不能为空")
    private String code;
}
