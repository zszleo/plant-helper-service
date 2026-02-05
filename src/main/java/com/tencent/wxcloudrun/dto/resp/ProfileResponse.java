package com.tencent.wxcloudrun.dto.resp;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

/**
 * 用户信息响应
 * @author zszleon
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
public class ProfileResponse {
    private String userId;
    private String openid;
    private String nickname;
    private String avatarUrl;
    private LocalDateTime createTime;
}
