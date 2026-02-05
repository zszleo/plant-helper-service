package com.tencent.wxcloudrun.dto.req;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

/**
 * 更新用户信息请求
 * @author zszleon
 */
@Data
@Schema(description = "更新用户信息请求")
public class ProfileRequest {
    @Schema(description = "用户昵称", example = "张三")
    private String nickname;
    
    @Schema(description = "用户头像URL", example = "https://picx.zhimg.com/v2-abed1a8c04700ba7d72b45195223e0ff_l.jpg?source=1def8aca")
    private String avatarUrl;
}
