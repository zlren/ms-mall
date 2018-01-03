package lab.zlren.mall.config.mybatis;

import com.zaxxer.hikari.HikariDataSource;
import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import javax.sql.DataSource;

/**
 * @author zlren
 * @date 2018-01-03
 */
@Configuration
@ConfigurationProperties("spring.datasource")
@Data
public class MyDataSource {

    private String url;
    private String username;
    private String password;
    private String driverClassName;

    @Bean
    public DataSource dataSource() {
        HikariDataSource hikariDataSource = new HikariDataSource();

        hikariDataSource.setJdbcUrl(url);
        hikariDataSource.setUsername(username);
        hikariDataSource.setPassword(password);
        hikariDataSource.setDriverClassName(driverClassName);
        hikariDataSource.setMaximumPoolSize(5);
        hikariDataSource.setMinimumIdle(1);

        return hikariDataSource;
    }
}
