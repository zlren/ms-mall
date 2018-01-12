package lab.zlren.mall.controller;

import lab.zlren.mall.common.rediskey.BasePrefix;
import lab.zlren.mall.common.rediskey.GoodsKey;
import lab.zlren.mall.common.response.Result;
import lab.zlren.mall.common.vo.GoodsDetailVO;
import lab.zlren.mall.common.vo.GoodsVO;
import lab.zlren.mall.entity.User;
import lab.zlren.mall.service.entity.GoodsService;
import lab.zlren.mall.service.util.RedisService;
import lab.zlren.mall.service.util.ResultService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.thymeleaf.spring4.context.SpringWebContext;
import org.thymeleaf.spring4.view.ThymeleafViewResolver;
import org.thymeleaf.util.StringUtils;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.List;

/**
 * @author zlren
 * @date 2018-01-08
 */
@Controller
@RequestMapping("goods")
@Slf4j
public class GoodController {

    @Autowired
    private GoodsService goodsService;

    @Autowired
    private RedisService redisService;

    @Autowired
    private ThymeleafViewResolver thymeleafViewResolver;

    @Autowired
    private ApplicationContext applicationContext;

    @Autowired
    private ResultService resultService;

    /**
     * 商品列表，这个列表大家看到的都一样，没有与特定用户相关的属性值
     *
     * @param model    model
     * @param user     user
     * @param request  request
     * @param response response
     * @return 商品列表html串
     */
    @GetMapping(value = "to_list", produces = "text/html")
    @ResponseBody
    public String toGoodsList(Model model, User user, HttpServletRequest request, HttpServletResponse response) {

        String goodsListHtml = redisService.get(GoodsKey.goodsListKey, "", String.class);

        if (StringUtils.isEmpty(goodsListHtml)) {

            List<GoodsVO> goodsVOList = goodsService.getGoodsVOList();
            model.addAttribute("goodsList", goodsVOList);

            model.addAttribute("user", user);

            goodsListHtml = generatePageHtml(request, response, model, "goods_list", GoodsKey.goodsListKey, "");
        }

        return goodsListHtml;
    }

    /**
     * 商品详情页
     *
     * @param goodsId id
     * @param user    user
     * @return 详情页
     */
    @GetMapping(value = "detail/{goodsId}")
    @ResponseBody
    public Result<GoodsDetailVO> getDetail(@PathVariable Long goodsId, User user) {

        GoodsVO goodsVO = goodsService.getGoodsVOByGoodsId(goodsId);

        long startAt = goodsVO.getStartDate().getTime();
        long endAt = goodsVO.getEndDate().getTime();
        long now = System.currentTimeMillis();

        Integer miaoshaStatus;
        Integer remainSeconds;

        if (now < startAt) {
            log.info("秒杀还没开始");
            miaoshaStatus = 0;
            remainSeconds = (int) ((startAt - now) / 1000);
        } else if (now > endAt) {
            log.info("秒杀已经结束");
            miaoshaStatus = 2;
            remainSeconds = -1;
        } else {
            log.info("正在进行");
            miaoshaStatus = 1;
            remainSeconds = 0;
        }

        GoodsDetailVO goodsDetailVO = new GoodsDetailVO()
                .setGoods(goodsVO)
                .setMiaoshaStatus(miaoshaStatus)
                .setRemainSeconds(remainSeconds)
                .setUser(user);

        log.info("结果：{}", goodsDetailVO);

        return resultService.success(goodsDetailVO);
    }


    /**
     * 手动渲染页面，返回html串，并存储到redis
     *
     * @param request  request
     * @param response response
     * @param model    model
     * @param page     page
     * @return 页面渲染后的html串
     */
    private String generatePageHtml(HttpServletRequest request, HttpServletResponse response, Model model, String page,
                                    BasePrefix basePrefix, String key) {
        SpringWebContext springWebContext = new SpringWebContext(
                request, response, request.getServletContext(), request.getLocale(), model.asMap(), applicationContext);
        String pageHtml = thymeleafViewResolver.getTemplateEngine().process(page, springWebContext);
        redisService.set(basePrefix, key, pageHtml);
        return pageHtml;
    }
}
