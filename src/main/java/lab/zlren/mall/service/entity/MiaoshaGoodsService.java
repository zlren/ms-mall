package lab.zlren.mall.service.entity;

import com.baomidou.mybatisplus.service.impl.ServiceImpl;
import lab.zlren.mall.common.rediskey.GoodsKey;
import lab.zlren.mall.common.rediskey.OrderKey;
import lab.zlren.mall.common.vo.GoodsVO;
import lab.zlren.mall.entity.MiaoshaGoods;
import lab.zlren.mall.entity.MiaoshaOrder;
import lab.zlren.mall.entity.OrderInfo;
import lab.zlren.mall.entity.User;
import lab.zlren.mall.mapper.MiaoshaGoodsMapper;
import lab.zlren.mall.service.util.Md5Service;
import lab.zlren.mall.service.util.RedisService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.script.ScriptEngine;
import javax.script.ScriptEngineManager;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.util.Date;
import java.util.Random;
import java.util.UUID;

/**
 * @author zlren
 * @date 2018-01-09
 */
@Service
@Slf4j
public class MiaoshaGoodsService extends ServiceImpl<MiaoshaGoodsMapper, MiaoshaGoods> {

    @Autowired
    private MiaoshaGoodsService miaoshaGoodsService;

    @Autowired
    private MiaoshaOrderService miaoshaOrderService;

    @Autowired
    private OrderInfoService orderInfoService;

    @Autowired
    private RedisService redisService;

    @Autowired
    private Md5Service md5Service;

    /**
     * 具体的秒杀操作，使用service的事务支持
     * 减库存
     * 下订单
     * 写入秒杀订单
     *
     * @param userId  用户id
     * @param goodsVO 秒杀商品
     * @return 订单
     */
    @Transactional(rollbackFor = Exception.class)
    public OrderInfo miaosha(Long userId, GoodsVO goodsVO) {

        // TODO：库存只考虑了miaosha_goods表，列是stock_count

        // 减库存
        boolean update = reduceStock(goodsVO.getId());

        if (update) {

            // 写入order
            OrderInfo orderInfo = new OrderInfo()
                    .setCreateDate(new Date())
                    .setDeliveryAddId(1L)
                    .setGoodsCount(1)
                    .setGoodsId(goodsVO.getId())
                    .setGoodsName(goodsVO.getGoodsName())
                    .setGoodsPrice(goodsVO.getMiaoshaPrice())
                    .setStatus(0)
                    .setUserId(userId);
            orderInfoService.insert(orderInfo);

            // orderInfo插入完成以后会完成id回填，orderInfo的id和miaoshaOrder的orderId是一回事

            // 写入miaosha-order
            MiaoshaOrder miaoshaOrder = new MiaoshaOrder()
                    .setOrderId(orderInfo.getId())
                    .setUserId(userId)
                    .setGoodsId(goodsVO.getId());
            miaoshaOrderService.insert(miaoshaOrder);

            // 写入redis的是miaoshaOrder
            redisService.set(OrderKey.miaoshaOrderKey, userId + "_" + goodsVO.getId(), miaoshaOrder);

            log.info("下单成功，订单详情是{}", orderInfo);

            return orderInfo;
        }

        setGoodsOver(goodsVO.getId());
        return null;
    }


    /**
     * 数据库减库存
     *
     * @param goodsId 秒杀商品id
     * @return 剪完库存之后的数量
     */
    private boolean reduceStock(Long goodsId) {
        // reduceStock的结果是影响的行数，大于0表示操作成功了
        return this.baseMapper.reduceStock(goodsId) > 0;
    }

    /**
     * 使用redis标识某个商品已经卖光了
     *
     * @param id goodsId
     */
    private void setGoodsOver(Long id) {
        redisService.set(GoodsKey.miaoshaGoodsOver, String.valueOf(id), 0);
    }

    /**
     * 商品充足
     *
     * @param id goodsId
     */
    public void setGoodsEnough(Long id) {
        redisService.set(GoodsKey.miaoshaGoodsOver, String.valueOf(id), 1);
    }

    /**
     * 查询是否卖光了
     *
     * @param id goodsId
     * @return true表示卖光了
     */
    private boolean getGoodsOver(Long id) {
        Integer over = redisService.get(GoodsKey.miaoshaGoodsOver, String.valueOf(id), Integer.class);
        return over == 0;
    }

    /**
     * 查询秒杀结果
     *
     * @param userId  用户
     * @param goodsId goodsId
     * @return 结果
     */
    public Long getMiaoshaResult(Long userId, Long goodsId) {

        MiaoshaOrder miaoshaOrder = orderInfoService.getMiaoshaOrderByUserIdGoodsId(userId, goodsId);

        if (miaoshaOrder != null) {
            // 秒杀成功，订单id
            return miaoshaOrder.getId();
        } else {
            if (getGoodsOver(goodsId)) {
                // 失败
                return -1L;
            } else {
                // 排队中
                return 0L;
            }
        }
    }

    /**
     * 秒杀接口校验
     *
     * @param user
     * @param goodsId
     * @param path
     * @return
     */
    public boolean checkPath(User user, Long goodsId, String path) {

        String realPath = redisService.get(GoodsKey.miaoshaGoodsPath, user.getId() + "_" + goodsId, String.class);
        if (realPath == null || realPath.length() <= 0 || !realPath.equals(path)) {
            return false;
        }

        return true;
    }

    /**
     * 生成秒杀地址
     *
     * @param user
     * @param goodsId
     * @return
     */
    public String createMiaoshaPath(User user, Long goodsId) {

        String path = md5Service.md5(UUID.randomUUID().toString(), "1234");
        // 不同用户对于不同商品的秒杀接口是不一样的，同一用户每次调用也不一样
        // 超时60秒
        redisService.set(GoodsKey.miaoshaGoodsPath, user.getId() + "_" + String.valueOf(goodsId), path);

        return path;
    }

    /**
     * 生成验证码图片
     *
     * @param user
     * @param goodsId
     * @return
     */
    public BufferedImage createVerifyCode(User user, long goodsId) {

        if (user == null || goodsId <= 0) {
            return null;
        }
        int width = 80;
        int height = 32;
        //create the image
        BufferedImage image = new BufferedImage(width, height, BufferedImage.TYPE_INT_RGB);
        Graphics g = image.getGraphics();
        // set the background color
        g.setColor(new Color(0xDCDCDC));
        g.fillRect(0, 0, width, height);
        // draw the border
        g.setColor(Color.black);
        g.drawRect(0, 0, width - 1, height - 1);
        // create a random instance to generate the codes
        Random rdm = new Random();
        // make some confusion
        for (int i = 0; i < 50; i++) {
            int x = rdm.nextInt(width);
            int y = rdm.nextInt(height);
            g.drawOval(x, y, 0, 0);
        }
        // generate a random code
        String verifyCode = generateVerifyCode(rdm);
        g.setColor(new Color(0, 100, 0));
        g.setFont(new Font("Candara", Font.BOLD, 24));
        g.drawString(verifyCode, 8, 24);
        g.dispose();
        // 把验证码存到redis中
        int rnd = calc(verifyCode);
        redisService.set(GoodsKey.miaoshaVerifyCode, user.getId() + "," + goodsId, rnd);
        // 输出图片
        return image;
    }

    /**
     * 校验验证码
     *
     * @param user
     * @param goodsId
     * @param verifyCode
     * @return
     */
    public boolean checkVerifyCode(User user, long goodsId, int verifyCode) {

        Integer codeOld = redisService.get(GoodsKey.miaoshaVerifyCode, user.getId() + "," + goodsId, Integer.class);

        if (codeOld == null || codeOld - verifyCode != 0) {
            return false;
        }

        redisService.del(GoodsKey.miaoshaVerifyCode, user.getId() + "," + goodsId);

        return true;
    }

    /**
     * 计算表达式结果
     *
     * @param exp
     * @return
     */
    private static int calc(String exp) {
        try {
            ScriptEngineManager manager = new ScriptEngineManager();
            ScriptEngine engine = manager.getEngineByName("JavaScript");
            return (Integer) engine.eval(exp);
        } catch (Exception e) {
            e.printStackTrace();
            return 0;
        }
    }

    private static char[] ops = new char[]{'+', '-', '*'};

    /**
     * 生成验证码
     *
     * @param rdm
     * @return
     */
    private String generateVerifyCode(Random rdm) {
        int num1 = rdm.nextInt(10);
        int num2 = rdm.nextInt(10);
        int num3 = rdm.nextInt(10);
        char op1 = ops[rdm.nextInt(3)];
        char op2 = ops[rdm.nextInt(3)];
        return "" + num1 + op1 + num2 + op2 + num3;
    }
}
