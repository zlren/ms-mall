package lab.zlren.mall.entity;

import com.baomidou.mybatisplus.annotations.TableField;
import com.baomidou.mybatisplus.annotations.TableId;
import com.baomidou.mybatisplus.annotations.TableName;
import com.baomidou.mybatisplus.enums.IdType;
import lombok.Data;
import lombok.experimental.Accessors;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Date;

/**
 * @author zlren
 * @since 2018-01-09
 */
@TableName("miaosha_goods")
@Data
@Accessors(chain = true)
public class MiaoshaGoods implements Serializable {

    private static final long serialVersionUID = 1L;

    /**
     * 主键id
     */
    @TableId(value = "id", type = IdType.AUTO)
    private Long id;
    /**
     * 商品id
     */
    @TableField("goods_id")
    private Long goodsId;
    /**
     * 秒杀价格
     */
    @TableField("miaosha_price")
    private BigDecimal miaoshaPrice;
    /**
     * 库存数量
     */
    @TableField("stock_count")
    private Integer stockCount;
    /**
     * 秒杀开始时间
     */
    @TableField("start_date")
    private Date startDate;
    /**
     * 秒杀结束时间
     */
    @TableField("end_date")
    private Date endDate;
}
