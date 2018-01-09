package lab.zlren.mall.common.rediskey;

import lombok.AllArgsConstructor;

/**
 * @author zlren
 * @date 2018-01-06
 */
@AllArgsConstructor
public abstract class BasePrefix implements KeyPrefix {

    private int expire;
    private String prefix;


    /**
     * 构造方法，永不过期
     *
     * @param prefix 前缀
     */
    BasePrefix(String prefix) {
        this(0, prefix);
    }

    /**
     * 默认0代表永不过期
     *
     * @return 过期时间
     */
    @Override
    public int getExpire() {
        return expire;
    }

    /**
     * 真正的前缀是：类名:二级名:值
     * @return
     */
    @Override
    public String getPrefix() {
        // 类名
        String name = getClass().getSimpleName();
        return name + ":" + prefix + ":";
    }
}
