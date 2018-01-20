package lab.zlren.mall.service.entity;

import com.baomidou.mybatisplus.service.impl.ServiceImpl;
import lab.zlren.mall.common.rediskey.GoodsKey;
import lab.zlren.mall.common.rediskey.OrderKey;
import lab.zlren.mall.common.vo.GoodsVO;
import lab.zlren.mall.entity.MiaoshaGoods;
import lab.zlren.mall.entity.MiaoshaOrder;
import lab.zlren.mall.entity.OrderInfo;
import lab.zlren.mall.mapper.MiaoshaGoodsMapper;
import lab.zlren.mall.service.util.RedisService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Date;

/**
 * @author zlren
 * @date 2018-01-09
 */
@Service
@Slf4j
public class MiaoshaGoodsService extends ServiceImpl<MiaoshaGoodsMapper, MiaoshaGoods> {

    @Autowired
    private MiaoshaGoodsService miaoshaGoodsService;

    @Autowired
    private MiaoshaOrderService miaoshaOrderService;

    @Autowired
    private OrderInfoService orderInfoService;

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

        // TODO：库存只考虑了miaosha_goods表，列是stock_count

        // 减库存
        boolean update = reduceStock(goodsVO.getId());

        if (update) {

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

            // orderInfo插入完成以后会完成id回填，orderInfo的id和miaoshaOrder的orderId是一回事

            // 写入miaosha-order
            MiaoshaOrder miaoshaOrder = new MiaoshaOrder()
                    .setOrderId(orderInfo.getId())
                    .setUserId(userId)
                    .setGoodsId(goodsVO.getId());
            miaoshaOrderService.insert(miaoshaOrder);

            // 写入redis的是miaoshaOrder
            redisService.set(OrderKey.miaoshaOrderKey, userId + "_" + goodsVO.getId(), miaoshaOrder);

            log.info("下单成功，订单详情是{}", orderInfo);

            return orderInfo;
        }

        setGoodsOver(goodsVO.getId());
        return null;
    }


    /**
     * 数据库减库存
     *
     * @param goodsId 秒杀商品id
     * @return 剪完库存之后的数量
     */
    private boolean reduceStock(Long goodsId) {
        // reduceStock的结果是影响的行数，大于0表示操作成功了
        return this.baseMapper.reduceStock(goodsId) > 0;
    }

    /**
     * 使用redis标识某个商品已经卖光了
     *
     * @param id goodsId
     */
    private void setGoodsOver(Long id) {
        redisService.set(GoodsKey.miaoshaGoodsOver, String.valueOf(id), 0);
    }

    /**
     * 商品充足
     *
     * @param id goodsId
     */
    public void setGoodsEnough(Long id) {
        redisService.set(GoodsKey.miaoshaGoodsOver, String.valueOf(id), 1);
    }

    /**
     * 查询是否卖光了
     *
     * @param id goodsId
     * @return true表示卖光了
     */
    private boolean getGoodsOver(Long id) {
        Integer over = redisService.get(GoodsKey.miaoshaGoodsOver, String.valueOf(id), Integer.class);
        return over == 0;
    }

    /**
     * 查询秒杀结果
     *
     * @param userId  用户
     * @param goodsId goodsId
     * @return 结果
     */
    public Long getMiaoshaResult(Long userId, Long goodsId) {

        MiaoshaOrder miaoshaOrder = orderInfoService.getMiaoshaOrderByUserIdGoodsId(userId, goodsId);

        if (miaoshaOrder != null) {
            // 秒杀成功，订单id
            return miaoshaOrder.getId();
        } else {
            if (getGoodsOver(goodsId)) {
                // 失败
                return -1L;
            } else {
                // 排队中
                return 0L;
            }
        }
    }
}
