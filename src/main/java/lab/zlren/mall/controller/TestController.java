package lab.zlren.mall.controller;

import lab.zlren.mall.common.redis.UserKey;
import lab.zlren.mall.common.response.Result;
import lab.zlren.mall.entity.User;
import lab.zlren.mall.service.RedisService;
import lab.zlren.mall.service.ResultService;
import lab.zlren.mall.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ResponseBody;

/**
 * 测试Controller
 *
 * @author zlren
 * @date 2018-01-03
 */
@Controller
public class TestController {

    @Autowired
    private ResultService resultService;

    @Autowired
    private UserService userService;

    @Autowired
    private RedisService redisService;

    @GetMapping("test")
    @ResponseBody
    public Result<String> test() {
        this.userService.insert(new User().setId(1).setName("zlren"));
        return resultService.success();
    }

    @GetMapping("thy")
    public String thy(Model model) {
        model.addAttribute("name", "zlren");
        return "hello";
    }

    @GetMapping("redis/get")
    @ResponseBody
    public Result<User> redis() {
        User user = redisService.get(UserKey.getById, String.valueOf(123), User.class);
        return resultService.success(user);
    }

    @GetMapping("redis/set")
    @ResponseBody
    public Result<String> redisSet() {
        int id = 123;
        String s = redisService.set(UserKey.getById, String.valueOf(123), new User().setId(id));
        return resultService.success(s);
    }
}
