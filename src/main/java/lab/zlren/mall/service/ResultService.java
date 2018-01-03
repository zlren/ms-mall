package lab.zlren.mall.service;

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

    public Result success() {
        return new Result(0, null, null);
    }

    public Result success(String msg) {
        return new Result(0, msg, null);
    }

    public Result success(Object data) {
        return success("", data);
    }

    public Result success(String msg, Object data) {
        return new Result(0, msg, data);
    }

    public Result failure(String msg) {
        return new Result(1, msg, null);
    }

    public Result failure(Integer code, String msg) {
        assert code != 0;
        return new Result(code, msg, null);
    }
}
