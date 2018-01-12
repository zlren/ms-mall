package lab.zlren.mall.service.entity;

import com.baomidou.mybatisplus.service.impl.ServiceImpl;
import lab.zlren.mall.common.rediskey.OrderKey;
import lab.zlren.mall.entity.MiaoshaOrder;
import lab.zlren.mall.entity.OrderInfo;
import lab.zlren.mall.mapper.OrderInfoMapper;
import lab.zlren.mall.service.util.RedisService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * @author zlren
 * @date 2018-01-09
 */
@Service
public class OrderInfoService extends ServiceImpl<OrderInfoMapper, OrderInfo> {

    @Autowired
    private RedisService redisService;

    /**
     * @param id
     * @param goodsId
     * @return
     */
    public MiaoshaOrder getMiaoshaOrderByUserIdGoodsId(Long id, Long goodsId) {
        return redisService.get(OrderKey.miaoshaOrderKey, id + "_" + goodsId, MiaoshaOrder.class);
    }
}
