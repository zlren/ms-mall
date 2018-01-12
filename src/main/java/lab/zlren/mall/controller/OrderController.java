package lab.zlren.mall.controller;

import lab.zlren.mall.common.response.CodeMsg;
import lab.zlren.mall.common.response.Result;
import lab.zlren.mall.common.vo.GoodsVO;
import lab.zlren.mall.common.vo.OrderDetailVO;
import lab.zlren.mall.entity.OrderInfo;
import lab.zlren.mall.entity.User;
import lab.zlren.mall.service.entity.GoodsService;
import lab.zlren.mall.service.entity.OrderInfoService;
import lab.zlren.mall.service.util.ResultService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

/**
 * @author zlren
 * @date 2018-01-12
 */
@Controller
@RequestMapping("order")
public class OrderController {

    @Autowired
    private OrderInfoService orderInfoService;

    @Autowired
    private ResultService resultService;

    @Autowired
    private GoodsService goodsService;

    /**
     * 订单详情
     *
     * @param user
     * @param orderId
     * @return
     */
    @RequestMapping("detail")
    @ResponseBody
    public Result<OrderDetailVO> info(User user, @RequestParam("orderId") long orderId) {

        OrderInfo order = orderInfoService.selectById(orderId);
        if (order == null) {
            return resultService.failure(CodeMsg.ORDER_NOT_EXIST);
        }

        GoodsVO goods = goodsService.getGoodsVOByGoodsId(order.getGoodsId());
        OrderDetailVO orderDetailVO = new OrderDetailVO();
        orderDetailVO.setOrder(order);
        orderDetailVO.setGoods(goods);

        return resultService.success(orderDetailVO);
    }
}
