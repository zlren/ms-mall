package lab.zlren.mall.common.exception;

import lab.zlren.mall.common.response.CodeMsg;
import lombok.Getter;

/**
 * 全局异常
 *
 * @author zlren
 * @date 2018-01-07
 */
public class GlobalException extends RuntimeException {

    @Getter
    private CodeMsg codeMsg;

    public GlobalException(CodeMsg codeMsg) {
        this.codeMsg = codeMsg;
    }
}
