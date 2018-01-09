package lab.zlren.mall.service.entity;

import com.baomidou.mybatisplus.service.impl.ServiceImpl;
import lab.zlren.mall.common.exception.GlobalException;
import lab.zlren.mall.common.rediskey.TokenKey;
import lab.zlren.mall.common.response.CodeMsg;
import lab.zlren.mall.common.vo.LoginVO;
import lab.zlren.mall.entity.User;
import lab.zlren.mall.mapper.UserMapper;
import lab.zlren.mall.service.util.Md5Service;
import lab.zlren.mall.service.util.RedisService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletResponse;
import java.util.UUID;

/**
 * @author zlren
 * @date 2018-01-03
 */
@Service
@Transactional(rollbackFor = Exception.class)
@Slf4j
public class UserService extends ServiceImpl<UserMapper, User> {

    public static final String COOKIE_NAME_TOKEN = "TOKEN";

    @Autowired
    private Md5Service md5Service;

    @Autowired
    private RedisService redisService;

    /**
     * 登录校验
     *
     * @param loginVO 登录参数
     * @return 校验结果
     */
    public boolean checkLogin(LoginVO loginVO, HttpServletResponse response) {

        User user = selectById(Long.valueOf(loginVO.getMobile()));
        if (user == null) {
            // 用户不存在是一种异常
            throw new GlobalException(CodeMsg.USER_NOT_EXIST);
        }

        // loginVO.getPassword()是客户端通过固定salt进行md5加密后的结果
        // 从数据库中取出对应用户的盐然后计算md5，和数据库中存储的值进行比对
        String password = md5Service.md5(loginVO.getPassword(), user.getSalt());

        // 密码匹配与否是结果，而不是异常
        if (password.equals(user.getPassword())) {
            // 第一次为这个用户生成一个token，并且这个token值在redis中不重复
            String token = UUID.randomUUID().toString().replace("-", "");
            while (redisService.exists(TokenKey.tokenKey, token)) {
                token = UUID.randomUUID().toString().replace("-", "");
            }

            addCookie(response, token, user);
            return true;
        }

        return false;
    }


    /**
     * 根据cookie取出对应的user
     *
     * @param cookieToken cookie中对应的TOKEN的值
     * @return user
     */
    public User getUserByToken(HttpServletResponse response, String cookieToken) {
        User user = redisService.get(TokenKey.tokenKey, cookieToken, User.class);
        if (user != null) {
            addCookie(response, cookieToken, user);
        }

        return user;
    }


    /**
     * 存储token、更新token的有效期
     * 存储会存到response和redis中
     *
     * @param response response
     * @param token    cookie中的TOKEN对应的值
     * @param user     user
     */
    private void addCookie(HttpServletResponse response, String token, User user) {

        // redis存储token，值为这个token背后的user
        redisService.set(TokenKey.tokenKey, token, user);

        Cookie cookie = new Cookie(COOKIE_NAME_TOKEN, token);
        cookie.setMaxAge(TokenKey.tokenKey.getExpire());
        cookie.setPath("/");
        response.addCookie(cookie);
    }
}