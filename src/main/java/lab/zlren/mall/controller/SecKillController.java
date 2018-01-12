package lab.zlren.mall.controller;

import lab.zlren.mall.common.response.CodeMsg;
import lab.zlren.mall.common.response.Result;
import lab.zlren.mall.common.vo.GoodsVO;
import lab.zlren.mall.entity.OrderInfo;
import lab.zlren.mall.entity.User;
import lab.zlren.mall.service.entity.GoodsService;
import lab.zlren.mall.service.entity.MiaoshaGoodsService;
import lab.zlren.mall.service.entity.OrderInfoService;
import lab.zlren.mall.service.util.ResultService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

/**
 * @author zlren
 * @date 2018-01-09
 */
@Controller
@RequestMapping("miaosha")
@Slf4j
public class SecKillController {

    @Autowired
    private GoodsService goodsService;

    @Autowired
    private OrderInfoService orderInfoService;

    @Autowired
    private MiaoshaGoodsService miaoshaGoodsService;

    @Autowired
    private ResultService resultService;

    @PostMapping("do_miaosha")
    @ResponseBody
    public Result<OrderInfo> doMiaoSha(User user, @RequestParam Long goodsId) {

        GoodsVO goodsVO = goodsService.getGoodsVOByGoodsId(goodsId);
        if (goodsVO.getStockCount() <= 0) {
            log.info("库存不足");
            return resultService.failure(CodeMsg.MIAOSHA_OVER);
        }

        if (orderInfoService.getMiaoshaOrderByUserIdGoodsId(user.getId(), goodsId) != null) {
            log.info("秒杀商品只能购买一件");
            return resultService.failure(CodeMsg.MIAOSHA_REPEATED);
        }

        // 秒杀
        OrderInfo orderInfo = miaoshaGoodsService.miaosha(user.getId(), goodsVO);
        return resultService.success(orderInfo);
    }
}
