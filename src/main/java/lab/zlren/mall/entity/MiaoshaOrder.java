package lab.zlren.mall.entity;

import com.baomidou.mybatisplus.enums.IdType;
import com.baomidou.mybatisplus.annotations.TableId;
import com.baomidou.mybatisplus.annotations.TableField;
import com.baomidou.mybatisplus.annotations.TableName;
import lombok.Data;
import lombok.experimental.Accessors;

import java.io.Serializable;

/**
 * @author zlren
 * @since 2018-01-09
 */
@TableName("miaosha_order")
@Data
@Accessors(chain = true)
public class MiaoshaOrder implements Serializable {

    private static final long serialVersionUID = 1L;

    /**
     * 秒杀订单id
     */
    @TableId(value = "id", type = IdType.AUTO)
    private Long id;
    /**
     * 用户id
     */
    @TableField("user_id")
    private Long userId;
    /**
     * 订单id
     */
    @TableField("order_id")
    private Long orderId;
    /**
     * 商品id
     */
    @TableField("goods_id")
    private Long goodsId;
}
