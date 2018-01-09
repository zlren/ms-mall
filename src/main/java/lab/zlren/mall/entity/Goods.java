package lab.zlren.mall.entity;

import com.baomidou.mybatisplus.annotations.TableField;
import com.baomidou.mybatisplus.annotations.TableId;
import com.baomidou.mybatisplus.enums.IdType;
import lombok.Data;
import lombok.experimental.Accessors;

import java.io.Serializable;
import java.math.BigDecimal;

/**
 * @author zlren
 * @since 2018-01-09
 */
@Data
@Accessors(chain = true)
public class Goods implements Serializable {

    private static final long serialVersionUID = 1L;

    /**
     * 商品id
     */
    @TableId(value = "id", type = IdType.AUTO)
    private Long id;
    /**
     * 商品名称
     */
    @TableField("goods_name")
    private String goodsName;
    /**
     * 商品标题
     */
    @TableField("goods_title")
    private String goodsTitle;
    /**
     * 商品图片
     */
    @TableField("goods_img")
    private String goodsImg;
    /**
     * 商品详情
     */
    @TableField("goods_detail")
    private String goodsDetail;
    /**
     * 商品单价
     */
    @TableField("goods_price")
    private BigDecimal goodsPrice;
    /**
     * 商品库存，-1表示没有限制
     */
    @TableField("goods_stock")
    private Integer goodsStock;
}
