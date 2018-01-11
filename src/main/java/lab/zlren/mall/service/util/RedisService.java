package lab.zlren.mall.service.util;

import lab.zlren.mall.common.rediskey.KeyPrefix;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import redis.clients.jedis.Jedis;
import redis.clients.jedis.JedisPool;

/**
 * RedisService
 *
 * @author zlren
 * @date 2018-01-05
 */
@Service
public class RedisService {

    @Autowired
    private JedisPool jedisPool;

    @Autowired
    private JsonService jsonService;

    /**
     * get
     *
     * @param prefix 前缀，包含过期时间
     * @param key    key
     * @param clazz  类
     * @param <T>    类型
     * @return value
     */
    public <T> T get(KeyPrefix prefix, String key, Class<T> clazz) {
        try (Jedis jedis = jedisPool.getResource()) {
            return jsonService.stringToBean(jedis.get(prefix.getPrefix() + key), clazz);
        }
    }

    /**
     * set
     *
     * @param prefix 前缀，包含过期时间，单位秒
     * @param key    key
     * @param value  value
     * @param <T>    类型
     * @return OK
     */
    public <T> String set(KeyPrefix prefix, String key, T value) {
        try (Jedis jedis = jedisPool.getResource()) {

            String realKey = prefix.getPrefix() + key;
            int expire = prefix.getExpire();

            if (expire <= 0) {
                return jedis.set(realKey, jsonService.beanToString(value));
            } else {
                return jedis.setex(realKey, prefix.getExpire(), jsonService.beanToString(value));
            }
        }
    }


    /**
     * 删除key
     *
     * @param prefix prefix
     * @param key    key
     * @return 结果
     */
    public Long del(KeyPrefix prefix, String key) {
        try (Jedis jedis = jedisPool.getResource()) {
            String realKey = prefix.getPrefix() + key;
            return jedis.del(realKey);
        }
    }

    /**
     * 判断key是否存在
     *
     * @param prefix 前缀
     * @param key    key
     * @return 是否存在
     */
    public boolean exists(KeyPrefix prefix, String key) {
        try (Jedis jedis = jedisPool.getResource()) {
            return jedis.exists(prefix.getPrefix() + key);
        }
    }


    /**
     * 自增操作
     *
     * @param prefix 前缀
     * @param key    key
     * @return 自增后的值
     */
    public Long incr(KeyPrefix prefix, String key) {
        try (Jedis jedis = jedisPool.getResource()) {
            return jedis.incr(prefix.getPrefix() + key);
        }
    }

    /**
     * 自减操作
     *
     * @param prefix 前缀
     * @param key    key
     * @return 自减后的值
     */
    public Long decr(KeyPrefix prefix, String key) {
        try (Jedis jedis = jedisPool.getResource()) {
            return jedis.decr(prefix.getPrefix() + key);
        }
    }
}
