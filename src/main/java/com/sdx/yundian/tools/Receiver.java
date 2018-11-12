package com.sdx.yundian.tools;

import com.sdx.yundian.dao.UserMsgRepository;
import com.sdx.yundian.entity.UserMsg;
import com.sdx.yundian.service.IMailService;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.Date;

@Component
public class Receiver {

    @Autowired
    private UserMsgRepository userMsgRepository;

    @Autowired
    private IMailService mailService;

    @RabbitListener(queues = "sdx.queue1")
    public String processMessage1(String msg) {
        System.out.println("sdx.queue1队列信息");
        return msg.toUpperCase();
    }

    @RabbitListener(queues = "sdx.queue2")
    public void processMessage2(String msg) {
        System.out.println("sdx.queue2队列信息");
    }

    @RabbitListener(queues = "sdx.queue3")//邮件处理队列
    public void processMessage3(UserMsg userMsg) {

        userMsg.setCreatedAt(new Date());
        UserMsg userMsg1 = userMsgRepository.save(userMsg);
        String content = userMsg1.getName() + "-" + userMsg1.getMobile() + "-" + userMsg1.getContent();
        System.out.println("==保存留言成功==");
        try {
            mailService.sendSimpleMail(userMsg1.getEmail(), "旭宓科技", content);
            System.out.println("==邮件发送成功==");
        } catch (Exception e) {
            System.out.println("==邮件发送失败==");
        }

    }

}
