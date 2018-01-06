package lab.zlren.mall.common.redis;

/**
 * 前缀（包含过期时间）
 *
 * @author zlren
 * @date 2018-01-06
 */
public interface KeyPrefix {

    /**
     * 过期时间
     *
     * @return 过期时间
     */
    int getExpire();

    /**
     * 前缀
     *
     * @return 前缀
     */
    String getPrefix();
}
