package lab.zlren.mall.controller;

import lab.zlren.mall.common.rediskey.GoodsKey;
import lab.zlren.mall.common.response.CodeMsg;
import lab.zlren.mall.common.response.Result;
import lab.zlren.mall.common.vo.GoodsVO;
import lab.zlren.mall.common.vo.MiaoshaRequest;
import lab.zlren.mall.entity.OrderInfo;
import lab.zlren.mall.entity.User;
import lab.zlren.mall.service.entity.GoodsService;
import lab.zlren.mall.service.entity.MiaoshaGoodsService;
import lab.zlren.mall.service.entity.OrderInfoService;
import lab.zlren.mall.service.mq.MqSender;
import lab.zlren.mall.service.util.RedisService;
import lab.zlren.mall.service.util.ResultService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import javax.imageio.ImageIO;
import javax.servlet.http.HttpServletResponse;
import javax.websocket.server.PathParam;
import java.awt.image.BufferedImage;
import java.io.OutputStream;
import java.util.List;

/**
 * @author zlren
 * @date 2018-01-09
 */
@Controller
@RequestMapping("miaosha")
@Slf4j
public class SecKillController implements InitializingBean {

    @Autowired
    private GoodsService goodsService;

    @Autowired
    private OrderInfoService orderInfoService;

    @Autowired
    private MiaoshaGoodsService miaoshaGoodsService;

    @Autowired
    private ResultService resultService;

    @Autowired
    private RedisService redisService;

    @Autowired
    private MqSender mqSender;

    /**
     * 初始化完成后会调用
     * 初始化完成后，把商品数量加载到缓存中
     */
    @Override
    public void afterPropertiesSet() {
        List<GoodsVO> goodsVOList = goodsService.getGoodsVOList();
        if (goodsVOList != null && goodsVOList.size() > 0) {
            goodsVOList.forEach(goodsVO -> {
                if (goodsVO.getStockCount() > 0) {
                    redisService.set(GoodsKey.miaoshaGoodsStock, String.valueOf(goodsVO.getId()), goodsVO.getStockCount());
                    miaoshaGoodsService.setGoodsEnough(goodsVO.getId());
                    log.info("加入缓存：{}", goodsVO);
                }
            });
        }
    }

    /**
     * 秒杀下单
     *
     * @param user    用户
     * @param goodsId 秒杀商品id
     * @return 排队中。。
     */
    @PostMapping("/{path}/do_miaosha")
    @ResponseBody
    public Result<OrderInfo> doMiaoSha(User user, @RequestParam Long goodsId, @PathVariable("path") String path) {

        // 校验path
        if (!miaoshaGoodsService.checkPath(user, goodsId, path)) {
            return resultService.failure(CodeMsg.PATH_ERROR);
        }

        // 预减库存
        // TODO，实际上后面来的每次请求都会把这个值减一，这是没有必要的，可以使用一个HashMap来优化，减少redis访问
        Long stock = redisService.decr(GoodsKey.miaoshaGoodsStock, String.valueOf(goodsId));
        if (stock < 0) {
            return resultService.failure(CodeMsg.MIAOSHA_OVER);
        }

        // 判重
        if (orderInfoService.getMiaoshaOrderByUserIdGoodsId(user.getId(), goodsId) != null) {
            log.info("秒杀商品只能购买一件");
            return resultService.failure(CodeMsg.MIAOSHA_REPEATED);
        }

        // 异步下单，请求入队
        mqSender.sendMiaoshaRequest(new MiaoshaRequest(user, goodsId));

        // 返回排队中
        return resultService.success(CodeMsg.LOADING);
    }


    /**
     * 查询秒杀结果
     *
     * @param user    用户
     * @param goodsId 商品id
     * @return 秒杀成功返回订单id，失败返回-1，0表示排队中
     */
    @GetMapping("result")
    @ResponseBody
    public Result<Long> getMiaoshaResult(User user, @RequestParam Long goodsId) {
        return resultService.success(miaoshaGoodsService.getMiaoshaResult(user.getId(), goodsId));
    }


    /**
     * 获取秒杀接口
     *
     * @param user    用户
     * @param goodsId 商品id
     * @return 秒杀接口参数
     */
    @GetMapping("path")
    @ResponseBody
    public Result<String> getMiaoshaPath(User user, @RequestParam Long goodsId, @PathParam("verifyCode") Integer verifyCode) {

        if (!miaoshaGoodsService.checkVerifyCode(user, goodsId, verifyCode)) {
            // 验证码错误
            return resultService.failure(CodeMsg.VERIFY_CODE_ERROR);
        }

        log.info("验证码校验通过");

        String path = miaoshaGoodsService.createMiaoshaPath(user, goodsId);
        return resultService.success(path);
    }


    /**
     * 生成数学验证码
     *
     * @param response res
     * @param user     用户
     * @param goodsId  商品id
     * @return
     */
    @RequestMapping(value = "verifyCode", method = RequestMethod.GET)
    @ResponseBody
    public Result<String> getMiaoshaVerifyCod(HttpServletResponse response, User user,
                                              @RequestParam("goodsId") long goodsId) {
        try {
            BufferedImage image = miaoshaGoodsService.createVerifyCode(user, goodsId);
            OutputStream out = response.getOutputStream();
            ImageIO.write(image, "JPEG", out);
            out.flush();
            out.close();
            return null;
        } catch (Exception e) {
            log.error(e.getMessage());
            return resultService.failure(CodeMsg.MIAOSHA_FAIL);
        }
    }
}
