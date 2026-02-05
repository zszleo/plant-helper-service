package com.tencent.wxcloudrun.auth;

import com.tencent.wxcloudrun.constant.AuthConstant;
import org.springframework.core.MethodParameter;
import org.springframework.stereotype.Component;
import org.springframework.web.bind.support.WebDataBinderFactory;
import org.springframework.web.context.request.NativeWebRequest;
import org.springframework.web.method.support.HandlerMethodArgumentResolver;
import org.springframework.web.method.support.ModelAndViewContainer;

import javax.servlet.http.HttpServletRequest;

/**
 * LoginUser参数解析器
 * 自动将Token验证后的用户信息注入到Controller方法参数
 * @author zszleon
 */
@Component
public class LoginUserArgumentResolver implements HandlerMethodArgumentResolver {
    
    @Override
    public boolean supportsParameter(MethodParameter parameter) {
        return parameter.getParameterType().equals(LoginUser.class);
    }
    
    @Override
    public Object resolveArgument(MethodParameter parameter, 
                                  ModelAndViewContainer mavContainer,
                                  NativeWebRequest webRequest, 
                                  WebDataBinderFactory binderFactory) {
        
        HttpServletRequest request = (HttpServletRequest) webRequest.getNativeRequest();
        
        // 从拦截器设置的属性中获取用户信息
        String openid = (String) request.getAttribute(AuthConstant.OPENID_ATTR);
        Long userId = (Long) request.getAttribute(AuthConstant.USER_ID_ATTR);
        
        if (openid == null || userId == null) {
            // 拦截器已验证Token，这里应该不会为null
            return null;
        }
        
        return new LoginUser(openid, userId);
    }
}