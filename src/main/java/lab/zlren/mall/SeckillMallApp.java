package lab.zlren.mall;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

/**
 * @author zlren
 * @date 2018-01-02
 */
@SpringBootApplication
public class SeckillMallApp { // extends SpringBootServletInitializer

    public static void main(String[] args) {
        SpringApplication.run(SeckillMallApp.class, args);
    }

    // @Override
    // protected SpringApplicationBuilder configure(SpringApplicationBuilder builder) {
    //     return builder.sources(SeckillMallApp.class);
    // }
}
