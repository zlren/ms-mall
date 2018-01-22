package lab.zlren.mall.config.access;

import lab.zlren.mall.common.rediskey.GoodsKey;
import lab.zlren.mall.common.response.CodeMsg;
import lab.zlren.mall.entity.User;
import lab.zlren.mall.service.entity.UserService;
import lab.zlren.mall.service.util.JsonService;
import lab.zlren.mall.service.util.RedisService;
import lab.zlren.mall.service.util.ResultService;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.web.method.HandlerMethod;
import org.springframework.web.servlet.handler.HandlerInterceptorAdapter;

import javax.servlet.ServletOutputStream;
import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

/**
 * 限流拦截器
 *
 * @author zlren
 * @date 2018-01-22
 */
@Component
@Slf4j
public class AccessLimitInterceptor extends HandlerInterceptorAdapter {

    @Autowired
    private UserService userService;

    @Autowired
    private JsonService jsonService;

    @Autowired
    private RedisService redisService;

    @Autowired
    private ResultService resultService;

    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {

        if (handler instanceof HandlerMethod) {
            HandlerMethod handlerMethod = (HandlerMethod) handler;
            AccessLimit accessLimit = handlerMethod.getMethodAnnotation(AccessLimit.class);

            if (accessLimit == null) {
                return true;
            }

            User user = getUser(request, response);
            UserContext.setUser(user);

            int seconds = accessLimit.seconds();
            int maxCount = accessLimit.maxCount();
            boolean needLogin = accessLimit.needLogin();

            String key = request.getRequestURI();

            if (needLogin) {
                if (user == null) {
                    render(response, CodeMsg.NOT_LOGIN);
                    return false;
                }
                key += ("_" + user.getId());
            }

            GoodsKey accessKey = GoodsKey.miaoshaAccessCount(seconds);
            Integer accessCount = redisService.get(accessKey, key, Integer.class);

            if (accessCount == null) {
                redisService.set(accessKey, key, 1);
            } else if (accessCount < maxCount) {
                redisService.incr(accessKey, key);
            } else {
                render(response, CodeMsg.ACCESS_COUNT_LIMIT);
                return false;
            }

            return true;
        }

        return super.preHandle(request, response, handler);
    }


    /**
     * 渲染错误
     *
     * @param response
     * @param codeMsg
     * @throws IOException
     */
    private void render(HttpServletResponse response, CodeMsg codeMsg) throws IOException {
        response.setContentType("application/json;charset=UTF-8");
        ServletOutputStream outputStream = response.getOutputStream();
        outputStream.write(jsonService.beanToString(resultService.failure(codeMsg)).getBytes("UTF-8"));
        outputStream.flush();
        outputStream.close();
    }


    /**
     * 获取用户信息
     *
     * @param request
     * @param response
     * @return
     */
    private User getUser(HttpServletRequest request, HttpServletResponse response) {

        // request或者cookie中携带token参数都可以
        String paramToken = request.getParameter(UserService.COOKIE_NAME_TOKEN);
        String cookieToken = getCookieValue(request, UserService.COOKIE_NAME_TOKEN);

        // 未登录状态，返回登录页面
        if (StringUtils.isEmpty(paramToken) && StringUtils.isEmpty(cookieToken)) {
            log.info("token值为空，用户未登录");
            // throw new GlobalException(CodeMsg.NOT_LOGIN);
            return null;
        }

        // 取出token值，paramToken的优先级高
        String token = StringUtils.isEmpty(paramToken) ? cookieToken : paramToken;
        User user = userService.getUserByTokenFromRedis(response, token);

        if (user != null) {
            log.info("当前用户：{}", user);
            return user;
        }

        log.info("无法从redis中查询到用户信息");

        // throw new GlobalException(CodeMsg.NOT_LOGIN);
        return null;
    }

    /**
     * 从cookie中取值
     *
     * @param request   request
     * @param cookieKey cookieKey
     * @return value
     */
    private String getCookieValue(HttpServletRequest request, String cookieKey) {

        Cookie[] cookies = request.getCookies();
        if (cookies == null || cookies.length == 0) {
            return null;
        }

        for (Cookie cookie : cookies) {
            if (cookie.getName().equals(cookieKey)) {
                return cookie.getValue();
            }
        }

        return null;
    }

}
