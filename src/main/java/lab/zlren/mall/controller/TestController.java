package lab.zlren.mall.controller;

import com.baomidou.mybatisplus.mapper.EntityWrapper;
import lab.zlren.mall.common.response.Result;
import lab.zlren.mall.entity.MiaoshaGoods;
import lab.zlren.mall.service.entity.MiaoshaGoodsService;
import lab.zlren.mall.service.entity.MiaoshaOrderService;
import lab.zlren.mall.service.entity.OrderInfoService;
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
    private MiaoshaGoodsService miaoshaGoodsService;

    @Autowired
    private MiaoshaOrderService miaoshaOrderService;

    @Autowired
    private OrderInfoService orderInfoService;

    @Autowired
    private ResultService resultService;

    @GetMapping("reset")
    @ResponseBody
    public Result<String> testMq() {

        MiaoshaGoods miaoshaGoods = new MiaoshaGoods().setStockCount(10);
        miaoshaGoodsService.update(miaoshaGoods, new EntityWrapper<>());

        miaoshaOrderService.delete(new EntityWrapper<>());

        orderInfoService.delete(new EntityWrapper<>());

        return resultService.success("先执行redis脚本，再启动应用程序后访问reset接口");
    }
}
