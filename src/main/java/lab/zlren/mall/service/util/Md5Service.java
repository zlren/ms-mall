package lab.zlren.mall.service.util;

import org.apache.commons.codec.digest.DigestUtils;
import org.springframework.stereotype.Service;

/**
 * 加密
 *
 * @author zlren
 * @date 2018-01-06
 */
@Service
public class Md5Service {

    /**
     * md5加密
     * 把网络传输过来的密码再加密一次
     *
     * @param str  被加密字符串
     * @param salt 盐
     * @return 加密后的结果
     */
    public String md5(String str, String salt) {
        return DigestUtils.md5Hex(str + salt);
    }

    /**
     * 用户输入的密码转成网络传输的密码
     *
     * @param str 用户输入的密码
     * @return 网络传输的密码
     */
    public String formToNetwork(String str) {
        // 这个规则需要和前端js保持一致
        String salt = "1a2b3c4d";
        String network = "" + salt.charAt(0) + salt.charAt(2) + str + salt.charAt(5) + salt.charAt(4);
        return DigestUtils.md5Hex(network);
    }
}
