package lab.zlren.mall.config.jedis;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import redis.clients.jedis.JedisPool;
import redis.clients.jedis.JedisPoolConfig;

/**
 * @author zlren
 * @date 2018-01-05
 */
@Configuration
@ConfigurationProperties("redis")
@Data
public class RedisConfig {

    private String host;
    private int port;
    private int timeout;
    private int poolMaxTotal;
    private int poolMaxIdle;
    /**
     * 单位秒
     */
    private int poolMaxWait;

    @Bean
    public JedisPool jedisPool() {

        JedisPoolConfig jedisPoolConfig = new JedisPoolConfig();
        jedisPoolConfig.setMaxTotal(poolMaxTotal);
        jedisPoolConfig.setMaxIdle(poolMaxIdle);
        jedisPoolConfig.setMaxWaitMillis(poolMaxWait * 1000);

        return new JedisPool(jedisPoolConfig, host, port, timeout * 1000);
    }
}
