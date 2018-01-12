package lab.zlren.mall.common.vo;

import lab.zlren.mall.entity.OrderInfo;
import lombok.Data;
import lombok.experimental.Accessors;

/**
 * @author zlren
 * @date 2018-01-12
 */
@Data
@Accessors(chain = true)
public class OrderDetailVO {

    private GoodsVO goods;
    private OrderInfo order;
}
