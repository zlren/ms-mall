package lab.zlren.mall.common.redis;

/**
 * @author zlren
 * @date 2018-01-06
 */
public class UserKey extends BasePrefix {

    private UserKey(int expireSeconds, String prefix) {
        super(expireSeconds, prefix);
    }

    private UserKey(String prefix) {
        super(prefix);
    }

    public static UserKey getById = new UserKey("id");
    public static UserKey getByName = new UserKey("name");
}
