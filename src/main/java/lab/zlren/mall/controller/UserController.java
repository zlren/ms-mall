package lab.zlren.mall.controller;

import lab.zlren.mall.common.response.Result;
import lab.zlren.mall.entity.User;
import lab.zlren.mall.service.util.ResultService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

/**
 * @author zlren
 * @date 2018-01-10
 */
@Controller
@RequestMapping("user")
public class UserController {

    @Autowired
    private ResultService resultService;

    @GetMapping("info")
    @ResponseBody
    public Result<User> getUserInfo(User user) {
        return resultService.success(user);
    }
}
