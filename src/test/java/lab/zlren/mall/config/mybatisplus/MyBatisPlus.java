package lab.zlren.mall.config.mybatisplus;

import com.baomidou.mybatisplus.mapper.EntityWrapper;
import lab.zlren.mall.entity.MiaoshaGoods;
import lab.zlren.mall.service.entity.MiaoshaGoodsService;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

/**
 * @author zlren
 * @date 2018-01-12
 */
@SpringBootTest
@RunWith(SpringRunner.class)
public class MyBatisPlus {

    @Autowired
    private MiaoshaGoodsService miaoshaGoodsService;

    @Test
    public void update() {
        MiaoshaGoods miaoshaGoods = new MiaoshaGoods().setStockCount(4);
        EntityWrapper<MiaoshaGoods> ew = new EntityWrapper<>();
        ew.eq("goods_id", 1);
        ew.gt(true, "stock_count", 0);
        // miaoshaGoodsService.update
        miaoshaGoodsService.update(miaoshaGoods, ew);
    }
}
