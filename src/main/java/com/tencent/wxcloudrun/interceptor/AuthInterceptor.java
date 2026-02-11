package com.tencent.wxcloudrun.interceptor;

import com.tencent.wxcloudrun.constant.AuthConstant;
import com.tencent.wxcloudrun.constant.CodeEnum;
import com.tencent.wxcloudrun.context.TokenContext;
import com.tencent.wxcloudrun.dto.resp.ApiResponse;
import cn.hutool.json.JSONUtil;
import lombok.extern.slf4j.Slf4j;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;
import org.springframework.web.servlet.HandlerInterceptor;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;


/**
 * 统一认证拦截器
 * @author zszleon
 */
@Slf4j
@Component
public class AuthInterceptor implements HandlerInterceptor {
    
    @Autowired
    private TokenContext tokenContext;
    
    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {
        String uri = request.getRequestURI();
        
        // 排除登录接口
        for (String excludePath : AuthConstant.EXCLUDE_PATHS) {
            if (uri.contains(excludePath)) {
                return true;
            }
        }
        
        // OPTIONS预检请求
        if ("OPTIONS".equalsIgnoreCase(request.getMethod())) {
            return true;
        }
        
        // 获取并验证Token
        String token = request.getHeader(AuthConstant.TOKEN_HEADER);
        if (!StringUtils.hasText(token)) {
            writeUnauthorizedResponse(response, "缺少认证Token");
            return false;
        }
        TokenContext.TokenInfo tokenInfo = tokenContext.validateToken(token);
        if (tokenInfo == null || tokenInfo.isExpired()) {
            writeUnauthorizedResponse(response, "Token无效或已过期");
            return false;
        }
        
        // 存储用户信息到请求属性（供参数解析器使用）
        request.setAttribute(AuthConstant.OPENID_ATTR, tokenInfo.getOpenid());
        request.setAttribute(AuthConstant.USER_ID_ATTR, tokenInfo.getUserId());
        
        return true;
    }
    
    private void writeUnauthorizedResponse(HttpServletResponse response, String message) throws IOException {
        response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
        response.setContentType("application/json;charset=UTF-8");
        ApiResponse<?> apiResponse = ApiResponse.of(CodeEnum.UNAUTHORIZED.getCode(), message, null);
        response.getWriter().write(JSONUtil.toJsonStr(apiResponse));
    }
}