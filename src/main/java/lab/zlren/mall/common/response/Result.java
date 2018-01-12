package lab.zlren.mall.common.response;

import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.extern.slf4j.Slf4j;

/**
 * 统一返回格式封装
 *
 * @author zlren
 * @date 2018-01-02
 */
@EqualsAndHashCode(callSuper = true)
@Data
@Slf4j
public class Result<T> extends CodeMsg {

    private T data;

    public Result(CodeMsg codeMsg, T data) {
        this(codeMsg.getCode(), codeMsg.getMsg(), data);
    }

    private Result(Integer code, String msg, T data) {
        super(code, msg);
        this.data = data;
    }

    public Result(CodeMsg codeMsg) {
        this(codeMsg, null);
    }
}
