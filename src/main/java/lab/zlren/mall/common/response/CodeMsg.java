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
    public static CodeMsg JSON_ERROR = new CodeMsg(5003, "JSON转换错误");

    /**
     * 鉴权模块
     */
    public static CodeMsg PASSWORD_ERROR = new CodeMsg(500100, "密码错误");
    public static CodeMsg NOT_LOGIN = new CodeMsg(500101, "未登录");
    public static CodeMsg USER_NOT_EXIST = new CodeMsg(500102, "用户不存在");

    /**
     * 商品模块
     */

    /**
     * 订单模块
     */
    public static CodeMsg ORDER_NOT_EXIST = new CodeMsg(500102, "订单不存在");

    /**
     * 秒杀模块
     */
    public static CodeMsg MIAOSHA_OVER = new CodeMsg(500200, "商品已经秒杀完毕，库存不足");
    public static CodeMsg MIAOSHA_REPEATED = new CodeMsg(500200, "不能重复秒杀");

    public CodeMsg fillArgs(Object... args) {
        int code = this.code;
        String msg = String.format(this.msg, args);
        return new CodeMsg(code, msg);
    }
}
