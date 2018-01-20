package lab.zlren.mall.controller;

import lab.zlren.mall.common.response.Result;
import lab.zlren.mall.service.mq.MqSender;
import lab.zlren.mall.service.util.ResultService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

/**
 * 测试Controller
 *
 * @author zlren
 * @date 2018-01-03
 */
@Controller
@RequestMapping("test")
public class TestController {

    @Autowired
    private MqSender mqSender;

    @Autowired
    private ResultService resultService;

    @GetMapping("reset")
    @ResponseBody
    public Result<String> testMq() {
        mqSender.send("大家好");
        return resultService.success("发送成功");
    }
}
