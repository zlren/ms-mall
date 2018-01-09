package lab.zlren.mall.common.exception;

import lab.zlren.mall.common.response.CodeMsg;
import lab.zlren.mall.common.response.Result;
import lab.zlren.mall.service.util.ResultService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.BindException;
import org.springframework.validation.ObjectError;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseBody;

import java.util.List;


/**
 * 全局异常
 *
 * @author zlren
 * @date 2018-01-07
 */
@ControllerAdvice
@ResponseBody
public class GlobalExceptionHandler {

    @Autowired
    private ResultService resultService;

    @ExceptionHandler(value = Exception.class)
    public Result<String> exceptionHandler(Exception e) {
        // e.printStackTrace();
        if (e instanceof GlobalException) {
            return resultService.failure(((GlobalException) e).getCodeMsg());
        } else if (e instanceof BindException) {

            // 错误的信息可能会有很多，是一个List的形式
            List<ObjectError> allErrors = ((BindException) e).getAllErrors();

            // 简单处理，只拿出了第一个
            // CodeMsg.VALID_BIND_ERROR的msg包含了一个%s，通过fillArgs方法可以将其填充，利用了String.format方法
            return resultService.failure(CodeMsg.VALID_BIND_ERROR.fillArgs(allErrors.get(0).getDefaultMessage()));
        }

        return resultService.failure(CodeMsg.PARAMS_ERROR);
    }

}
