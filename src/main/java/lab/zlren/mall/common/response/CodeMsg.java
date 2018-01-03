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

    /**
     * 鉴权模块
     */
    public static CodeMsg LOGIN_FAILURE = new CodeMsg(500100, "登录失败");

    /**
     * 商品模块
     */

    /**
     * 订单模块
     */

    /**
     * 秒杀模块
     */
}
