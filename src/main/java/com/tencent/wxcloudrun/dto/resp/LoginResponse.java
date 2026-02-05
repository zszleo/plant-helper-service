package com.tencent.wxcloudrun.dto.resp;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * 登录响应
 * @author zszleon
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
@Schema(description = "登录响应")
public class LoginResponse {
    @Schema(description = "登录令牌", example = "eyJhbGciOiJIUzI1NiIsInR5cCI6IkpXVCJ9...")
    private String token;
    
    @Schema(description = "是否为新用户", example = "true")
    private Boolean isNewUser;
}
