package com.tencent.wxcloudrun.config;

import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Contact;
import io.swagger.v3.oas.models.info.Info;
import io.swagger.v3.oas.models.info.License;
import io.swagger.v3.oas.models.security.SecurityRequirement;
import io.swagger.v3.oas.models.security.SecurityScheme;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * OpenAPI 配置类
 * @author zszleon
 */
@Configuration
public class OpenApiConfig {

    @Bean
    public OpenAPI plantHelperOpenAPI() {
        return new OpenAPI()
                .info(new Info()
                        .title("植物种植助手小程序后端API文档")
                        .description("植物种植助手小程序后端服务接口文档，基于Spring Boot 2.7.18实现。")
                        .version("v1.0")
                        .contact(new Contact()
                                .name("zszleon")
                                .email("2079973876@qq.com"))
                        .license(new License()
                                .name("MIT License")
                                .url("https://opensource.org/licenses/MIT")))
                .addSecurityItem(new SecurityRequirement().addList("TokenAuth"))
                .components(new io.swagger.v3.oas.models.Components()
                        .addSecuritySchemes("TokenAuth",
                                new SecurityScheme()
                                        .name("X-Token")
                                        .type(SecurityScheme.Type.APIKEY)
                                        .in(SecurityScheme.In.HEADER)
                                        .description("用户认证Token，通过微信登录接口获取")));
    }
}