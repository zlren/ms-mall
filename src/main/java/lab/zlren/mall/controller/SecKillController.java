package lab.zlren.mall.controller;

import lab.zlren.mall.common.rediskey.GoodsKey;
import lab.zlren.mall.common.response.CodeMsg;
import lab.zlren.mall.common.response.Result;
import lab.zlren.mall.common.vo.GoodsVO;
import lab.zlren.mall.common.vo.MiaoshaRequest;
import lab.zlren.mall.entity.OrderInfo;
import lab.zlren.mall.entity.User;
import lab.zlren.mall.service.entity.GoodsService;
import lab.zlren.mall.service.entity.MiaoshaGoodsService;
import lab.zlren.mall.service.entity.OrderInfoService;
import lab.zlren.mall.service.mq.MqSender;
import lab.zlren.mall.service.util.RedisService;
import lab.zlren.mall.service.util.ResultService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * @author zlren
 * @date 2018-01-09
 */
@Controller
@RequestMapping("miaosha")
@Slf4j
public class SecKillController implements InitializingBean {

    @Autowired
    private GoodsService goodsService;

    @Autowired
    private OrderInfoService orderInfoService;

    @Autowired
    private MiaoshaGoodsService miaoshaGoodsService;

    @Autowired
    private ResultService resultService;

    @Autowired
    private RedisService redisService;

    @Autowired
    private MqSender mqSender;

    /**
     * 初始化完成后会调用
     * 初始化完成后，把商品数量加载到缓存中
     */
    @Override
    public void afterPropertiesSet() {
        List<GoodsVO> goodsVOList = goodsService.getGoodsVOList();
        if (goodsVOList != null && goodsVOList.size() > 0) {
            goodsVOList.forEach(goodsVO -> {
                if (goodsVO.getStockCount() > 0) {
                    redisService.set(GoodsKey.miaoshaGoodsStock, String.valueOf(goodsVO.getId()), goodsVO.getStockCount());
                    miaoshaGoodsService.setGoodsEnough(goodsVO.getId());
                }
            });
        }
    }

    /**
     * 秒杀下单
     *
     * @param user    用户
     * @param goodsId 秒杀商品id
     * @return 排队中。。
     */
    @PostMapping("do_miaosha")
    @ResponseBody
    public Result<OrderInfo> doMiaoSha(User user, @RequestParam Long goodsId) {

        // 预减库存
        // TODO，实际上后面来的每次请求都会把这个值减一，这是没有必要的，可以使用一个HashMap来优化，减少redis访问
        Long stock = redisService.decr(GoodsKey.miaoshaGoodsStock, String.valueOf(goodsId));
        if (stock < 0) {
            return resultService.failure(CodeMsg.MIAOSHA_OVER);
        }

        // 判重
        if (orderInfoService.getMiaoshaOrderByUserIdGoodsId(user.getId(), goodsId) != null) {
            log.info("秒杀商品只能购买一件");
            return resultService.failure(CodeMsg.MIAOSHA_REPEATED);
        }

        // 异步下单，请求入队
        mqSender.sendMiaoshaRequest(new MiaoshaRequest(user, goodsId));

        // 返回排队中
        return resultService.success(CodeMsg.LOADING);
    }


    /**
     * 查询秒杀结果
     *
     * @param user    用户
     * @param goodsId 商品id
     * @return 秒杀成功返回订单id，失败返回-1，0表示排队中
     */
    @GetMapping("result")
    @ResponseBody
    public Result<Long> getMiaoshaResult(User user, @RequestParam Long goodsId) {
        return resultService.success(miaoshaGoodsService.getMiaoshaResult(user.getId(), goodsId));
    }
}
