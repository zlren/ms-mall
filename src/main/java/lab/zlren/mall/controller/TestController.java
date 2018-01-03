package lab.zlren.mall.controller;

import lab.zlren.mall.common.response.Result;
import lab.zlren.mall.service.ResultService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * @author zlren
 * @date 2018-01-03
 */
@RestController
public class TestController {

    @Autowired
    private ResultService resultService;

    @GetMapping("test")
    public Result test() {
        return resultService.success("哈哈哈");
    }
}
