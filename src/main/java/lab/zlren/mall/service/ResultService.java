package lab.zlren.mall.service;

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

    public <T> Result<T> success() {
        return new Result<>(CodeMsg.SUCCESS, null);
    }

    public <T> Result<T> success(T data) {
        return new Result<>(CodeMsg.SUCCESS, data);
    }

    public <T> Result<T> failure(String msg) {
        return new Result<>(CodeMsg.SERVER_ERROR, null);
    }
}
