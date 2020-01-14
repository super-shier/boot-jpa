package com.shier.common.boot.jpa;

import com.shier.common.boot.jpa.service.MailService;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import javax.annotation.Resource;

/**
 * @Author: liyunbiao
 * @Date: 2019/9/4 2:25 PM
 * @description
 */
@RunWith(SpringRunner.class)
@SpringBootTest
public class MailTest {
    private static final Logger logger = LoggerFactory.getLogger(MailTest.class);
    @Resource
    private MailService mailService;

    @Test
    public void sendEmail() {
        mailService.sendEmail("871192593@qq.com", "这是测试", "这是测试零零落落零零落落");
    }

    @Test
    public void sendAnnexEmail() {
        mailService.sendAnnexEmail("871192593@qq.com", "这是测试", "这是测试零零落落零零落落", "/Users/qinmei/Downloads/李云标back.jpeg", "测试");
    }

    @Test
    public void sendImageMail() {
        //mailService.sendImageMail("871192593@qq.com", "helloWorld", "<h1 style='color:red'>helloWorld</h1><img src='cid:test001'/>", "/Users/qinmei/Downloads/李云标back.jpeg", "test001");
        mailService.sendImageMail("871192593@qq.com", "helloWorld", "helloWorld", "/Users/qinmei/Downloads/李云标back.jpeg");
    }

}
