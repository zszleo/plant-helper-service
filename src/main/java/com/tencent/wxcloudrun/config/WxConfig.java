package com.tencent.wxcloudrun.config;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

/**
 * 微信小程序配置
 * @author zszleon
 */
@Data
@Component
@ConfigurationProperties(prefix = "wx")
public class WxConfig {
    private String appid;
    private String secret;
}
