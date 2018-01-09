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
@TableName("order_info")
@Data
@Accessors(chain = true)
public class OrderInfo implements Serializable {

    private static final long serialVersionUID = 1L;

    /**
     * 订单id
     */
    @TableId(value = "id", type = IdType.AUTO)
    private Long id;
    /**
     * 用户id
     */
    @TableField("user_id")
    private Long userId;
    /**
     * 商品id
     */
    @TableField("goods_id")
    private Long goodsId;
    /**
     * 收货地址id
     */
    @TableField("delivery_add_id")
    private Long deliveryAddId;
    /**
     * 冗余商品名称
     */
    @TableField("goods_name")
    private String goodsName;
    /**
     * 商品库存
     */
    @TableField("goods_count")
    private Integer goodsCount;
    /**
     * 商品单价
     */
    @TableField("goods_price")
    private BigDecimal goodsPrice;
    /**
     * 1pc, 2android, 3ios
     */
    @TableField("order_channel")
    private Integer orderChannel;
    /**
     * 订单状态，0新建未支付，1已支付，2已发货，3已收货，4已退款，5已完成
     */
    private Integer status;
    /**
     * 订单创建时间
     */
    @TableField("create_date")
    private Date createDate;
    /**
     * 支付时间
     */
    @TableField("pay_date")
    private Date payDate;
}
