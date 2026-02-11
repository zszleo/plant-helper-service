package com.tencent.wxcloudrun.dto.resp;

import com.fasterxml.jackson.annotation.JsonFormat;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Date;
import java.util.List;

/**
 * 用户信息响应
 * @author zszleon
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Schema(description = "用户信息响应")
public class ProfileResponse {
    @Schema(description = "用户ID", example = "123456789")
    private String userId;
    
    @Schema(description = "微信openid", example = "o6_bmjrPTlm6_2sgVt7hMZOPfL2M")
    private String openid;
    
    @Schema(description = "用户昵称", example = "张三")
    private String nickname;
    
    @Schema(description = "用户头像URL", example = "https://thirdwx.qlogo.cn/...")
    private String avatarUrl;
    
    @Schema(description = "创建时间", example = "1770785605969")
    @JsonFormat(shape = JsonFormat.Shape.NUMBER)
    private Date createTime;
}
