package lab.zlren.mall.common.rediskey;

/**
 * 页面缓存
 *
 * @author zlren
 * @date 2018-01-08
 */
public class GoodsKey extends BasePrefix {

    /**
     * 页面缓存的时间较短
     */
    private static int GOODS_EXPIRE = 60;

    /**
     * 默认2天
     *
     * @param prefix 前缀
     */
    private GoodsKey(int expire, String prefix) {
        super(expire, prefix);
    }

    /**
     * 永不过期
     *
     * @param prefix 前缀
     */
    private GoodsKey(String prefix) {
        super(prefix);
    }

    public static GoodsKey goodsListKey = new GoodsKey(GOODS_EXPIRE, "goodsList");
    public static GoodsKey goodsDetailKey = new GoodsKey(GOODS_EXPIRE, "goodsDetail");
    public static GoodsKey miaoshaGoodsStock = new GoodsKey("miaoshaGoodsStock");
    public static GoodsKey miaoshaGoodsOver = new GoodsKey("miaoshaGoodsOver");
    public static GoodsKey miaoshaGoodsPath = new GoodsKey(60, "miaoshaGoodsPath");
    public static GoodsKey miaoshaVerifyCode = new GoodsKey(300, "miaoshaVerifyCode");
}
