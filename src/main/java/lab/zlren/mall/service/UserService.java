package lab.zlren.mall.service;

import com.baomidou.mybatisplus.service.impl.ServiceImpl;
import lab.zlren.mall.entity.User;
import lab.zlren.mall.mapper.UserMapper;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * @author zlren
 * @date 2018-01-03
 */
@Service
@Transactional(rollbackFor = Exception.class)
public class UserService extends ServiceImpl<UserMapper, User> {

}
