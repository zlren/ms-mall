package lab.zlren.mall.service.entity;

import com.baomidou.mybatisplus.service.impl.ServiceImpl;
import lab.zlren.mall.common.vo.GoodsVO;
import lab.zlren.mall.entity.Goods;
import lab.zlren.mall.mapper.GoodsMapper;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * @author zlren
 */
@Service
public class GoodsService extends ServiceImpl<GoodsMapper, Goods> {

    /**
     * 商品列表
     *
     * @return list
     */
    public List<GoodsVO> getGoodsVOList() {
        return baseMapper.selectGoodsListBySql();
    }

    /**
     * 商品详情
     *
     * @param goodsId id
     * @return 详情
     */
    public GoodsVO getGoodsVOByGoodsId(Long goodsId) {
        return baseMapper.selectGoodsVoByGoodsId(goodsId);
    }
}
