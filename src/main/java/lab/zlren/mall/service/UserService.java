package lab.zlren.mall.service;

import com.baomidou.mybatisplus.service.impl.ServiceImpl;
import lab.zlren.mall.common.exception.GlobalException;
import lab.zlren.mall.common.rediskey.TokenKey;
import lab.zlren.mall.common.response.CodeMsg;
import lab.zlren.mall.common.vo.LoginVO;
import lab.zlren.mall.entity.User;
import lab.zlren.mall.mapper.UserMapper;
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
            addCookie(response, user);
            return true;
        }

        return false;
    }


    /**
     * 根据cookie取出对应的user
     *
     * @param cookieToken cookie值
     * @return user
     */
    public User getUserByToken(HttpServletResponse response, String cookieToken) {
        User user = redisService.get(TokenKey.tokenKey, cookieToken, User.class);
        if (user != null) {
            addCookie(response, user);
        }

        return user;
    }


    /**
     * 存储token、更新token的有效期
     *
     * @param response response
     * @param user     user
     */
    private void addCookie(HttpServletResponse response, User user) {

        // TODO 每次生成一个新的token值作为key，这样好吗
        String token = UUID.randomUUID().toString().replace("-", "");

        // redis存储token，值为这个token背后的user
        redisService.set(TokenKey.tokenKey, token, user);

        Cookie cookie = new Cookie(COOKIE_NAME_TOKEN, token);
        cookie.setMaxAge(TokenKey.tokenKey.getExpire());
        cookie.setPath("/");
        response.addCookie(cookie);
    }
}
