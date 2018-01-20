package lab.zlren.mall.common.vo;

import lab.zlren.mall.entity.User;
import lombok.AllArgsConstructor;
import lombok.Data;

/**
 * 秒杀请求
 *
 * @author zlren
 * @date 2018-01-20
 */
@Data
@AllArgsConstructor
public class MiaoshaRequest {

    private User user;
    private Long goodsId;
}
