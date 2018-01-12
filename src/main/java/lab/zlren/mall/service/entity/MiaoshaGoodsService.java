package lab.zlren.mall.service.entity;

import com.baomidou.mybatisplus.mapper.EntityWrapper;
import com.baomidou.mybatisplus.service.impl.ServiceImpl;
import lab.zlren.mall.common.rediskey.OrderKey;
import lab.zlren.mall.common.vo.GoodsVO;
import lab.zlren.mall.entity.MiaoshaGoods;
import lab.zlren.mall.entity.MiaoshaOrder;
import lab.zlren.mall.entity.OrderInfo;
import lab.zlren.mall.mapper.MiaoshaGoodsMapper;
import lab.zlren.mall.service.util.RedisService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Date;

/**
 * @author zlren
 * @date 2018-01-09
 */
@Service
public class MiaoshaGoodsService extends ServiceImpl<MiaoshaGoodsMapper, MiaoshaGoods> {

    @Autowired
    private MiaoshaGoodsService miaoshaGoodsService;

    @Autowired
    private MiaoshaOrderService miaoshaOrderService;

    @Autowired
    private OrderInfoService orderInfoService;

    @Autowired
    private GoodsService goodsService;

    @Autowired
    private RedisService redisService;

    /**
     * 具体的秒杀操作，使用service的事务支持
     * 减库存
     * 下订单
     * 写入秒杀订单
     *
     * @param userId  用户id
     * @param goodsVO 秒杀商品
     * @return 订单
     */
    @Transactional(rollbackFor = Exception.class)
    public OrderInfo miaosha(Long userId, GoodsVO goodsVO) {

        // miaosha-goods减库存
        MiaoshaGoods miaoshaGoods = new MiaoshaGoods().setStockCount(goodsVO.getStockCount() - 1);
        EntityWrapper<MiaoshaGoods> ew = new EntityWrapper<>();
        ew.eq("goods_id", goodsVO.getId());
        ew.gt(true, "stock_count", 0);
        miaoshaGoodsService.update(miaoshaGoods, ew);

        // // goods减库存
        // Goods goods = new Goods().setGoodsStock(goodsVO.getGoodsStock() - 1);
        // EntityWrapper<Goods> ew2 = new EntityWrapper<>();
        // ew2.eq("id", goodsVO.getId());
        // ew2.gt(true, "goods_stock", 0);
        // goodsService.update(goods, ew2);

        // 写入order
        OrderInfo orderInfo = new OrderInfo()
                .setCreateDate(new Date())
                .setDeliveryAddId(1L)
                .setGoodsCount(1)
                .setGoodsId(goodsVO.getId())
                .setGoodsName(goodsVO.getGoodsName())
                .setGoodsPrice(goodsVO.getMiaoshaPrice())
                .setStatus(0)
                .setUserId(userId);
        orderInfoService.insert(orderInfo);

        // 写入miaosha-order
        MiaoshaOrder miaoshaOrder = new MiaoshaOrder()
                .setOrderId(orderInfo.getId())
                .setUserId(userId)
                .setGoodsId(goodsVO.getId());
        miaoshaOrderService.insert(miaoshaOrder);

        // 写入redis的是miaoshaOrder
        redisService.set(OrderKey.miaoshaOrderKey, userId + "_" + goodsVO.getId(), miaoshaOrder);

        return orderInfo;
    }
}
