package lab.zlren.mall.controller;

import lab.zlren.mall.entity.User;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

/**
 * @author zlren
 * @date 2018-01-08
 */
@Controller
@RequestMapping("goods")
@Slf4j
public class GoodController {

    @GetMapping("to_list")
    public String toGoodsList(Model model, User user) {
        model.addAttribute("user", user);
        return "goods_list";
    }
}
