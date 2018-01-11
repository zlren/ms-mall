package lab.zlren.mall.controller;

import com.baomidou.mybatisplus.mapper.EntityWrapper;
import lab.zlren.mall.common.response.CodeMsg;
import lab.zlren.mall.common.vo.GoodsVO;
import lab.zlren.mall.entity.OrderInfo;
import lab.zlren.mall.entity.User;
import lab.zlren.mall.service.entity.GoodsService;
import lab.zlren.mall.service.entity.MiaoshaGoodsService;
import lab.zlren.mall.service.entity.OrderInfoService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

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

    @GetMapping("do_miaosha")
    public String doMiaoSha(Model model, User user, @RequestParam Long goodsId) {

        log.info("user是：{}", user.getId());

        GoodsVO goodsVO = goodsService.getGoodsVOByGoodsId(goodsId);
        if (goodsVO.getStockCount() <= 0) {
            model.addAttribute("errmsg", CodeMsg.MIAOSHA_OVER.getMsg());
            return "miaosha_fail";
        }

        if (orderInfoService.selectCount(new EntityWrapper<>(
                new OrderInfo().setUserId(user.getId()).setGoodsId(goodsId))) > 0) {
            model.addAttribute("errmsg", CodeMsg.MIAOSHA_REPEATED);
            return "miaosha_fail";
        }

        // 秒杀
        OrderInfo orderInfo = miaoshaGoodsService.miaosha(user.getId(), goodsVO);
        model.addAttribute("orderInfo", orderInfo);
        model.addAttribute("goods", goodsVO);

        return "order_detail";
    }
}
