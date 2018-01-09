package lab.zlren.mall.controller;

import lab.zlren.mall.common.response.CodeMsg;
import lab.zlren.mall.common.response.Result;
import lab.zlren.mall.common.vo.LoginVO;
import lab.zlren.mall.service.ResultService;
import lab.zlren.mall.service.UserService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.servlet.http.HttpServletResponse;

/**
 * @author zlren
 * @date 2018-01-06
 */
@Controller
@RequestMapping("login")
@Slf4j
public class LoginController {

    @Autowired
    private UserService userService;

    @Autowired
    private ResultService resultService;

    /**
     * 跳转到登录页面
     *
     * @return login页面
     */
    @RequestMapping("to_login")
    public String toLogin() {
        return "login";
    }

    /**
     * 登录校验
     *
     * @param loginVO 表单信息 @Validated参数校验，如果有异常会交给GlobalExceptionHandler去处理
     * @return 校验结果
     */
    @RequestMapping("do_login")
    @ResponseBody
    public Result<String> doLogin(@Validated LoginVO loginVO,
                                  HttpServletResponse response) {

        if (userService.checkLogin(loginVO, response)) {
            return resultService.success();
        }

        return resultService.failure(CodeMsg.PASSWORD_ERROR);
    }
}
