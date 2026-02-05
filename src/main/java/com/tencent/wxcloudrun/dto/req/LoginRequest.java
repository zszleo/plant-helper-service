package com.tencent.wxcloudrun.dto.req;

import lombok.Data;
import javax.validation.constraints.NotBlank;

/**
 * 登录请求
 * @author zszleon
 */
@Data
public class LoginRequest {
    @NotBlank(message = "code不能为空")
    private String code;
}
