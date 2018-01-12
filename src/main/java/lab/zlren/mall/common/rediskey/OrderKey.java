package lab.zlren.mall.common.rediskey;

/**
 * @author zlren
 * @date 2018-01-06
 */
public class OrderKey extends BasePrefix {

    private OrderKey(String prefix) {
        super(prefix);
    }

    public static OrderKey miaoshaOrderKey = new OrderKey("miaosha");
}
