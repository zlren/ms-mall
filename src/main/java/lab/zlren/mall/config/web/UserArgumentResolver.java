package lab.zlren.mall.config.web;

import lab.zlren.mall.common.exception.GlobalException;
import lab.zlren.mall.common.response.CodeMsg;
import lab.zlren.mall.entity.User;
import lab.zlren.mall.service.UserService;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.MethodParameter;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.support.WebDataBinderFactory;
import org.springframework.web.context.request.NativeWebRequest;
import org.springframework.web.method.support.HandlerMethodArgumentResolver;
import org.springframework.web.method.support.ModelAndViewContainer;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * 用来填充controller参数列表的User值
 *
 * @author zlren
 * @date 2018-01-09
 */
@Slf4j
@Service
public class UserArgumentResolver implements HandlerMethodArgumentResolver {

    @Autowired
    private UserService userService;

    @Override
    public boolean supportsParameter(MethodParameter methodParameter) {
        Class<?> type = methodParameter.getParameterType();
        return type == User.class;
    }

    @Override
    public Object resolveArgument(MethodParameter methodParameter, ModelAndViewContainer modelAndViewContainer,
                                  NativeWebRequest nativeWebRequest, WebDataBinderFactory webDataBinderFactory) throws Exception {

        HttpServletRequest request = nativeWebRequest.getNativeRequest(HttpServletRequest.class);
        HttpServletResponse response = nativeWebRequest.getNativeResponse(HttpServletResponse.class);

        String cookieToken = getCookieValue(request, UserService.COOKIE_NAME_TOKEN);

        // 未登录状态，返回登录页面
        if (StringUtils.isEmpty(cookieToken)) {
            log.info("cookie的值为空，用户未登录");
            throw new GlobalException(CodeMsg.NOT_LOGIN);
        }

        // 根据cookie取出对应的用户
        User user = userService.getUserByToken(response, cookieToken);

        if (user != null) {
            log.info("当前用户：{}", user);
            return user;
        }

        log.info("无法从redis中查询到用户信息");

        throw new GlobalException(CodeMsg.NOT_LOGIN);
    }

    /**
     * 从cookie中取值
     *
     * @param request   request
     * @param cookieKey cookieKey
     * @return value
     */
    private String getCookieValue(HttpServletRequest request, String cookieKey) {

        for (Cookie cookie : request.getCookies()) {
            if (cookie.getName().equals(cookieKey)) {
                return cookie.getValue();
            }
        }

        return null;
    }
}
