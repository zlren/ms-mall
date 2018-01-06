package lab.zlren.mall.controller;

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
    public Result<Long> redis() {
        Long aLong = redisService.get("key1", Long.class);
        return resultService.success(aLong);
    }
}
