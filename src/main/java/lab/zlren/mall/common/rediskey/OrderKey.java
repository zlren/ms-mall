package lab.zlren.mall.common.rediskey;

/**
 * @author zlren
 * @date 2018-01-06
 */
public class OrderKey extends BasePrefix {

    public OrderKey(int expireSeconds, String prefix) {
        super(expireSeconds, prefix);
    }
}
