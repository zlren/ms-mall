package lab.zlren.mall.service.util;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import lab.zlren.mall.common.exception.GlobalException;
import lab.zlren.mall.common.response.CodeMsg;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.io.IOException;

/**
 * @author zlren
 * @date 2018-01-05
 */
@Service
public class JsonService {

    @Autowired
    private ObjectMapper objectMapper;

    /**
     * 序列化
     *
     * @param bean 被序列化对象
     * @param <T>  对象类型
     * @return 序列化后的字符串
     */
    public <T> String beanToString(T bean) {
        if (bean == null) {
            return null;
        }
        Class<?> beanClass = bean.getClass();
        if (beanClass == int.class || beanClass == Integer.class) {
            return String.valueOf(bean);
        } else if (beanClass == String.class) {
            return (String) bean;
        } else if (beanClass == long.class || beanClass == Long.class) {
            return String.valueOf(bean);
        } else {
            try {
                return objectMapper.writeValueAsString(bean);
            } catch (JsonProcessingException e) {
                throw new GlobalException(CodeMsg.JSON_ERROR);
            }
        }
    }


    /**
     * 反序列化
     *
     * @param str   被反序列化的字符串
     * @param clazz 类型
     * @param <T>   泛型
     * @return bean
     */
    public <T> T stringToBean(String str, Class<T> clazz) {

        if (str == null || str.length() <= 0 || clazz == null) {
            return null;
        }

        if (clazz == int.class || clazz == Integer.class) {
            return (T) Integer.valueOf(str);
        } else if (clazz == String.class) {
            return (T) str;
        } else if (clazz == long.class || clazz == Long.class) {
            return (T) Long.valueOf(str);
        } else {
            try {
                return objectMapper.readValue(str, clazz);
            } catch (IOException e) {
                throw new GlobalException(CodeMsg.JSON_ERROR);
            }
        }
    }
}
