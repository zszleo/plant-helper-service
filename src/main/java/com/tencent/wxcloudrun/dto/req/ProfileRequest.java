package com.tencent.wxcloudrun.dto.req;

import lombok.Data;

/**
 * 更新用户信息请求
 * @author zszleon
 */
@Data
public class ProfileRequest {
    private String nickname;
    private String avatarUrl;
}
