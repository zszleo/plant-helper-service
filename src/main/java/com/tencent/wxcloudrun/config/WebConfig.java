package com.tencent.wxcloudrun.config;

import com.tencent.wxcloudrun.constant.AuthConstant;
import com.tencent.wxcloudrun.auth.LoginUserArgumentResolver;
import com.tencent.wxcloudrun.interceptor.AuthInterceptor;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.method.support.HandlerMethodArgumentResolver;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

import javax.annotation.Resource;
import java.util.List;


/**
 * Web配置类
 * @author zszleon
 */
@Configuration
public class WebConfig implements WebMvcConfigurer {
    
    @Resource
    private AuthInterceptor authInterceptor;
    
    @Resource
    private LoginUserArgumentResolver loginUserArgumentResolver;
    
    @Override
    public void addInterceptors(InterceptorRegistry registry) {
        registry.addInterceptor(authInterceptor)
                .addPathPatterns("/api/**")
                .excludePathPatterns(AuthConstant.EXCLUDE_PATHS);
    }
    
    @Override
    public void addArgumentResolvers(List<HandlerMethodArgumentResolver> resolvers) {
        resolvers.add(loginUserArgumentResolver);
    }
}