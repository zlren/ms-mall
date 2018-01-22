package lab.zlren.mall.config.access;

import lab.zlren.mall.entity.User;

/**
 * @author zlren
 * @date 2018-01-22
 */
public class UserContext {

    private static ThreadLocal<User> userThreadLocal = new ThreadLocal<>();

    public static void setUser(User user) {
        userThreadLocal.set(user);
    }

    public static User getUser() {
        return userThreadLocal.get();
    }
}
