package lab.zlren.mall.service.entity;

import lab.zlren.mall.common.rediskey.TokenKey;
import lab.zlren.mall.entity.User;
import lab.zlren.mall.service.util.Md5Service;
import lab.zlren.mall.service.util.RedisService;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import java.io.File;
import java.io.IOException;
import java.io.RandomAccessFile;
import java.util.Date;
import java.util.UUID;

/**
 * @author zlren
 * @date 2018-01-11
 */
@SpringBootTest
@RunWith(SpringRunner.class)
public class UserUtil {

    @Autowired
    private UserService userService;

    @Autowired
    private Md5Service md5Service;

    @Autowired
    private RedisService redisService;

    @Test
    public void generateUser() throws IOException {

        File file = new File("/Users/zlren/Desktop/jmeter/tokens.txt");
        RandomAccessFile raf = new RandomAccessFile(file, "rw");
        file.createNewFile();
        raf.seek(0);

        int count = 1000;
        // salt是每个用户自己的slat，这里为了方便将其定死了
        String salt = "1234";
        for (int i = 0; i < count; i++) {

            User user = new User()
                    .setId(13000000000L + i)
                    .setNickname("nickname-" + i)
                    .setRegisterDate(new Date())
                    .setSalt(salt)
                    // 123456是每个用户的明文密码
                    .setPassword(md5Service.md5(md5Service.formToNetwork("123456"), salt));

            // 插入数据库
            userService.insert(user);

            // 生成token，保存到redis和文件中
            String token = UUID.randomUUID().toString().replace("-", "");
            while (redisService.exists(TokenKey.tokenKey, token)) {
                token = UUID.randomUUID().toString().replace("-", "");
            }

            // redis
            redisService.set(TokenKey.tokenKey, token, user);

            // file
            raf.seek(raf.length());
            raf.write((user.getId() + "," + token).getBytes());
            raf.write("\r\n".getBytes());
        }
        raf.close();
    }
}
