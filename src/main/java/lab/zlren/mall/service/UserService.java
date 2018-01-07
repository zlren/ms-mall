package lab.zlren.mall.service;

import com.baomidou.mybatisplus.service.impl.ServiceImpl;
import lab.zlren.mall.common.exception.GlobalException;
import lab.zlren.mall.common.response.CodeMsg;
import lab.zlren.mall.common.vo.LoginVO;
import lab.zlren.mall.entity.User;
import lab.zlren.mall.mapper.UserMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * @author zlren
 * @date 2018-01-03
 */
@Service
@Transactional(rollbackFor = Exception.class)
public class UserService extends ServiceImpl<UserMapper, User> {

    @Autowired
    private Md5Service md5Service;

    /**
     * 登录校验
     *
     * @param loginVO 登录参数
     * @return 校验结果
     */
    public boolean checkLogin(LoginVO loginVO) {

        User user = selectById(Long.valueOf(loginVO.getMobile()));
        if (user == null) {
            // 用户不存在是一种异常
            throw new GlobalException(CodeMsg.USER_NOT_EXIST);
        }

        // loginVO.getPassword()是客户端通过固定salt进行md5加密后的结果
        // 从数据库中取出对应用户的盐然后计算md5，和数据库中存储的值进行比对
        String password = md5Service.md5(loginVO.getPassword(), user.getSalt());

        // 密码匹配与否是结果，而不是异常
        return password.equals(user.getPassword());
    }
}
