package lab.zlren.mall.common.response;

import lombok.AllArgsConstructor;
import lombok.Data;

/**
 * 分模块构建错误状态
 *
 * @author zlren
 * @date 2018-01-03
 */
@Data
@AllArgsConstructor
public class CodeMsg {

    private Integer code;
    private String msg;

    /**
     * 通用异常
     */
    public static CodeMsg SUCCESS = new CodeMsg(0, "success");
    public static CodeMsg SERVER_ERROR = new CodeMsg(500, "server-error");
    public static CodeMsg PARAMS_ERROR = new CodeMsg(5001, "参数异常");
    public static CodeMsg VALID_BIND_ERROR = new CodeMsg(5002, "参数绑定异常：%s");

    /**
     * 鉴权模块
     */
    public static CodeMsg PASSWORD_ERROR = new CodeMsg(500100, "密码错误");
    public static CodeMsg USER_NOT_EXIST = new CodeMsg(500101, "用户不存在");

    /**
     * 商品模块
     */

    /**
     * 订单模块
     */

    /**
     * 秒杀模块
     */

    public CodeMsg fillArgs(Object... args) {
        int code = this.code;
        String msg = String.format(this.msg, args);
        return new CodeMsg(code, msg);
    }
}
