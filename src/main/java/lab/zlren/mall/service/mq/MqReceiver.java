package lab.zlren.mall.service.mq;

import lab.zlren.mall.common.vo.GoodsVO;
import lab.zlren.mall.common.vo.MiaoshaRequest;
import lab.zlren.mall.config.mq.MqConfig;
import lab.zlren.mall.entity.OrderInfo;
import lab.zlren.mall.entity.User;
import lab.zlren.mall.service.entity.GoodsService;
import lab.zlren.mall.service.entity.MiaoshaGoodsService;
import lab.zlren.mall.service.entity.OrderInfoService;
import lab.zlren.mall.service.util.JsonService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * @author zlren
 * @date 2018-01-17
 */
@Service
@Slf4j
public class MqReceiver {

    @Autowired
    private JsonService jsonService;

    @Autowired
    private GoodsService goodsService;

    @Autowired
    private OrderInfoService orderInfoService;

    @Autowired
    private MiaoshaGoodsService miaoshaGoodsService;

    /**
     * 监听秒杀队列请求
     *
     * @param msg
     */
    @RabbitListener(queues = MqConfig.MIAOSHA_QUEUE)
    public void receive(String msg) {

        log.info("收到消息：{}", msg);
        MiaoshaRequest miaoshaRequest = jsonService.stringToBean(msg, MiaoshaRequest.class);

        User user = miaoshaRequest.getUser();
        Long goodsId = miaoshaRequest.getGoodsId();

        // goods表有个goodsStock
        // miaoshaGoods表有个stockCount
        GoodsVO goodsVO = goodsService.getGoodsVOByGoodsId(goodsId);
        if (goodsVO.getStockCount() <= 0) {
            log.error("库存不足");
            return;
        }

        // 判重
        if (orderInfoService.getMiaoshaOrderByUserIdGoodsId(user.getId(), goodsId) != null) {
            log.error("秒杀商品只能购买一件");
            return;
        }

        OrderInfo orderInfo = miaoshaGoodsService.miaosha(user.getId(), goodsVO);
        // return resultService.success(orderInfo);
    }
}
