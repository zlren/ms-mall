package lab.zlren.mall.mapper;

import com.baomidou.mybatisplus.mapper.BaseMapper;
import lab.zlren.mall.entity.MiaoshaGoods;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Update;

/**
 * @author zlren
 * @since 2018-01-09
 */
public interface MiaoshaGoodsMapper extends BaseMapper<MiaoshaGoods> {

    /**
     * 减库存
     *
     * @param goodsId 商品id
     * @return 影响的行数
     */
    @Update("update miaosha_goods set stock_count = stock_count - 1 where goods_id = #{goodsId} and stock_count > 0")
    int reduceStock(@Param("goodsId") Long goodsId);
}
