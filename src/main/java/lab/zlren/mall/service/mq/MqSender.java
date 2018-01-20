package lab.zlren.mall.service.mq;

import lab.zlren.mall.common.vo.MiaoshaRequest;
import lab.zlren.mall.config.mq.MqConfig;
import lab.zlren.mall.service.util.JsonService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.amqp.core.AmqpTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * @author zlren
 * @date 2018-01-17
 */
@Service
@Slf4j
public class MqSender {

    @Autowired
    private JsonService jsonService;

    @Autowired
    private AmqpTemplate amqpTemplate;

    public void send(Object msg) {
        String msgStr = jsonService.beanToString(msg);
        amqpTemplate.convertAndSend(MqConfig.QUEUE, msgStr);
        log.info("发送消息：{}", msgStr);
    }

    /**
     * 队列中加入秒杀请求
     *
     * @param miaoshaRequest 秒杀请求pojo类封装
     */
    public void sendMiaoshaRequest(MiaoshaRequest miaoshaRequest) {
        String miaoshaRequestStr = jsonService.beanToString(miaoshaRequest);
        log.info("秒杀请求入队：{}", miaoshaRequestStr);
        amqpTemplate.convertAndSend(MqConfig.MIAOSHA_QUEUE, miaoshaRequestStr);
    }
}
