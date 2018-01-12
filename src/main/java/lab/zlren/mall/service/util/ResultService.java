package lab.zlren.mall.service.util;

import lab.zlren.mall.common.response.CodeMsg;
import lab.zlren.mall.common.response.Result;
import org.springframework.stereotype.Service;

/**
 * 快速构建Result
 *
 * @author zlren
 * @date 2018-01-02
 */
@Service
public class ResultService {

    /**
     * 成功
     *
     * @param <T>
     * @return
     */
    public <T> Result<T> success() {
        return new Result<>(CodeMsg.SUCCESS, null);
    }

    /**
     * 成功
     *
     * @param data
     * @param <T>
     * @return
     */
    public <T> Result<T> success(T data) {
        return new Result<>(CodeMsg.SUCCESS, data);
    }

    /**
     * 错误
     *
     * @param codeMsg 具体错误信息
     * @return
     */
    public <T> Result<T> failure(CodeMsg codeMsg) {
        return new Result<>(codeMsg);
    }
}
