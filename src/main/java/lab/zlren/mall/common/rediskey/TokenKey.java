package lab.zlren.mall.common.rediskey;

/**
 * token前缀
 *
 * @author zlren
 * @date 2018-01-08
 */
public class TokenKey extends BasePrefix {

    private static int TOKEN_EXPIRE = 3600 * 24 * 2;

    /**
     * 默认2天
     *
     * @param prefix 前缀
     */
    private TokenKey(int expire, String prefix) {
        super(expire, prefix);
    }

    public static TokenKey tokenKey = new TokenKey(TOKEN_EXPIRE, "token");
}
