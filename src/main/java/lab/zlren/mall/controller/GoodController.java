package lab.zlren.mall.controller;

import lab.zlren.mall.common.vo.GoodsVO;
import lab.zlren.mall.entity.User;
import lab.zlren.mall.service.entity.GoodsService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;

import java.util.List;

/**
 * @author zlren
 * @date 2018-01-08
 */
@Controller
@RequestMapping("goods")
@Slf4j
public class GoodController {

    @Autowired
    private GoodsService goodsService;

    @GetMapping("to_list")
    public String toGoodsList(Model model, User user) {

        List<GoodsVO> goodsVOList = goodsService.getGoodsVOList();
        model.addAttribute("goodsList", goodsVOList);

        model.addAttribute("user", user);
        return "goods_list";
    }

    /**
     * 商品详情页
     *
     * @param goodsId id
     * @param model   model
     * @param user    user
     * @return 详情页
     */
    @GetMapping("to_detail/{goods_id}")
    public String getDetail(@PathVariable("goods_id") Long goodsId, Model model, User user) {

        GoodsVO goodsVO = goodsService.getGoodsVOByGoodsId(goodsId);

        model.addAttribute("goods", goodsVO);

        long startAt = goodsVO.getStartDate().getTime();
        long endAt = goodsVO.getEndDate().getTime();
        long now = System.currentTimeMillis();

        int miaoshaStatus;
        long remainSeconds;

        if (now < startAt) {
            log.info("秒杀还没开始");
            // 秒杀还没开始
            miaoshaStatus = 0;
            remainSeconds = (startAt - now) / 1000;

        } else if (now > endAt) {
            log.info("秒杀已经结束");
            // 秒杀已经结束
            miaoshaStatus = 2;
            remainSeconds = -1;
        } else {
            log.info("正在进行");
            // 秒杀正在进行
            miaoshaStatus = 1;
            remainSeconds = 0;
        }

        model.addAttribute("miaoshaStatus", miaoshaStatus);
        model.addAttribute("remainSeconds", remainSeconds);

        model.addAttribute("user", user);

        return "goods_detail";
    }
}
