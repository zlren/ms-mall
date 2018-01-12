package lab.zlren.mall.common.vo;

import lab.zlren.mall.entity.User;
import lombok.Data;
import lombok.experimental.Accessors;

/**
 * @author zlren
 * @date 2018-01-12
 */
@Data
@Accessors(chain = true)
public class GoodsDetailVO {

    private GoodsVO goods;
    private Integer miaoshaStatus;
    private Integer remainSeconds;
    private User user;
}
