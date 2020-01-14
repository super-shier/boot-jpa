package com.shier.common.boot.jpa.service.impl;

import com.shier.common.boot.jpa.service.MailService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.io.FileSystemResource;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Service;

import javax.mail.internet.MimeMessage;
import java.io.File;

/**
 * @Author: liyunbiao
 * @Date: 2019/9/4 2:21 PM
 * @description
 */
@Service
public class MailServiceImpl implements MailService {
    private static final Logger logger = LoggerFactory.getLogger(MailServiceImpl.class);
    @Autowired
    private JavaMailSender mailSender;

    @Value("${mail.fromMail.addr}")
    private String from;

    @Override
    public void sendEmail(String to, String subject, String content) {
        SimpleMailMessage simpleMailMessage = new SimpleMailMessage();
        simpleMailMessage.setFrom(from);
        simpleMailMessage.setTo(to);
        simpleMailMessage.setSubject(subject);
        simpleMailMessage.setText(content);
        mailSender.send(simpleMailMessage);
    }

    @Override
    public void sendAnnexEmail(String to, String subject, String content, String filePath, String fileName) {
        try {
            MimeMessage mimeMessage = mailSender.createMimeMessage();
            MimeMessageHelper messageHelper = new MimeMessageHelper(mimeMessage, true);
            messageHelper.setFrom(from);
            messageHelper.setTo(to);
            messageHelper.setSubject(subject);
            messageHelper.setText(content);
            messageHelper.addAttachment(fileName, new File(filePath));
            mailSender.send(mimeMessage);
        } catch (Exception e) {
            logger.info("*********邮件发送失败to:{},subject:{}", to, subject);
            e.printStackTrace();
        }
    }

    @Override
    public void sendImageMail(String to, String subject, String content, String imgPath) {
        try {
            MimeMessage message = mailSender.createMimeMessage();
            MimeMessageHelper helper = new MimeMessageHelper(message, true);
            helper.setFrom(from);
            helper.setTo(to);
            helper.setSubject(subject);
            helper.setText("<html><body><div align='center'>" + content + "</div><img height='100%' width='100%' src='cid:a'/></body></html>", true);
            FileSystemResource res = new FileSystemResource(new File(imgPath));
            //可以发送带多个图片的邮件
            helper.addInline("a", res);
            mailSender.send(message);
        } catch (Exception e) {
            logger.info("*********邮件发送失败to:{},subject:{}", to, subject);
            e.printStackTrace();
        }

    }

}
