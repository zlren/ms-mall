package lab.zlren.mall.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import redis.clients.jedis.Jedis;
import redis.clients.jedis.JedisPool;

/**
 * @author zlren
 * @date 2018-01-05
 */
@Service
public class RedisService {

    @Autowired
    private JedisPool jedisPool;

    @Autowired
    private JsonService jsonService;

    public <T> T get(String key, Class<T> clazz) {
        try (Jedis jedis = jedisPool.getResource()) {
            return jsonService.stringToBean(jedis.get(key), clazz);
        }
    }

    public <T> String set(String key, T value) {
        try (Jedis jedis = jedisPool.getResource()) {
            return jedis.set(key, jsonService.beanToString(value));
        }
    }
}
