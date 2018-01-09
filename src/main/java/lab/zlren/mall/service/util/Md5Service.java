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
     *
     * @param str  被加密字符串
     * @param salt 盐
     * @return 加密后的结果
     */
    public String md5(String str, String salt) {
        return DigestUtils.md5Hex(str + salt);
    }
}
