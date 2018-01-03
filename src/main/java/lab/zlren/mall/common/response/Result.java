package lab.zlren.mall.common.response;

import lombok.Data;
import lombok.extern.slf4j.Slf4j;

/**
 * @author zlren
 * @date 2018-01-02
 */
@Data
@Slf4j
public class Result {

    private Integer code;
    private String msg;
    private Object data;

    public Result(Integer code, String msg, Object data) {
        this.code = code;
        this.msg = msg;
        this.data = data;

        log.info("{}", this);
    }
}
