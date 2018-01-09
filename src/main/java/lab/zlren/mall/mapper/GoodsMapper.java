package lab.zlren.mall.mapper;

import com.baomidou.mybatisplus.mapper.BaseMapper;
import lab.zlren.mall.common.vo.GoodsVO;
import lab.zlren.mall.entity.Goods;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;

import java.util.List;

/**
 * @author zlren
 * @since 2018-01-09
 */
public interface GoodsMapper extends BaseMapper<Goods> {

    /**
     * goods表左连接miaosha_goods表
     *
     * @return 商品列表
     */
    @Select("select g.*, mg.stock_count, mg.start_date, mg.end_date, mg.miaosha_price " +
            "from miaosha_goods mg left join goods g " +
            "on mg.goods_id = g.id")
    List<GoodsVO> selectGoodsListBySql();

    /**
     * 根据id查
     *
     * @param goodsId 商品id
     * @return 商品详情
     */
    @Select("select g.*,mg.stock_count, mg.start_date, mg.end_date,mg.miaosha_price from miaosha_goods mg left join goods g on " +
            "mg.goods_id = g.id where g.id = #{goodsId}")
    GoodsVO selectGoodsVoByGoodsId(@Param("goodsId") long goodsId);
}
