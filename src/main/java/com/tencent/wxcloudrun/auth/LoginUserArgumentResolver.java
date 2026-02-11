package com.tencent.wxcloudrun.auth;

import com.tencent.wxcloudrun.constant.HeaderConstant;
import com.tencent.wxcloudrun.dao.UserMapper;
import com.tencent.wxcloudrun.model.User;
import org.springframework.core.MethodParameter;
import org.springframework.stereotype.Component;
import org.springframework.web.bind.support.WebDataBinderFactory;
import org.springframework.web.context.request.NativeWebRequest;
import org.springframework.web.method.support.HandlerMethodArgumentResolver;
import org.springframework.web.method.support.ModelAndViewContainer;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;

/**
 * LoginUser参数解析器
 * 自动将Token验证后的用户信息注入到Controller方法参数
 *
 * @author zszleon
 */
@Component
public class LoginUserArgumentResolver implements HandlerMethodArgumentResolver {
    @Resource
    private UserMapper userMapper;

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

        // 从请求头中获取用户信息
        String openid = request.getHeader(HeaderConstant.X_WX_OPENID);
        User user = userMapper.findByOpenid(openid);
        if (user == null) {
            user = new User();
            user.setOpenid(openid);
            user.setNickname("微信用户");
            userMapper.insert(user);
        }
        Long userId = user.getId();

        return new LoginUser(openid, userId);
    }
}