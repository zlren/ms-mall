package lab.zlren.mall.common.vo;

import lab.zlren.mall.entity.Goods;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.math.BigDecimal;
import java.util.Date;

/**
 * @author zlren
 * @date 2018-01-09
 */
@EqualsAndHashCode(callSuper = true)
@Data
public class GoodsVO extends Goods {

    private Integer stockCount;
    private Date startDate;
    private Date endDate;
    private BigDecimal miaoshaPrice;
    private Integer status;
}
