package com.shier.common.boot.jpa.schedule;

import com.alibaba.fastjson.JSON;
import com.google.common.collect.Lists;
import com.shier.common.boot.jpa.common.utils.DateUtil;
import com.shier.common.boot.jpa.service.MailService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;
import org.springframework.util.CollectionUtils;

import javax.annotation.Resource;
import java.util.Date;
import java.util.List;
import java.util.Random;

import static com.shier.common.boot.jpa.common.utils.DateUtil.DateTimeToStr;

/**
 * @Author: liyunbiao
 * @Date: 2019/8/15 4:48 PM
 * @description 定时签到
 */
@Component
public class SignSchedule {
    private static final Logger logger = LoggerFactory.getLogger(SignSchedule.class);
    private static final Random random = new Random();

    @Resource
    private MailService mailService;
    /**
     * 图片文件地址
     * /home/jpa/jpa/image/
     */
    @Value("${mail.send.image.path}")
    private String mailSendImagePath;
    /**
     * 发送邮箱1819510836@qq.com
     */
    @Value("${mail.send.to.user}")
    private String mailSendToUser;
    /**
     * 不发送日期
     */
    @Value("${mail.send.stop.days}")
    private String mailSendStopDays;
    /**
     * 发送邮箱列表
     */
    @Value("${mail.send.to.users}")
    private String mailSendToUsers;

    //每隔 5000 毫秒执行一次
    @Scheduled(fixedRate = 3600000)
    public void reportCurrentTime() {
        logger.info("每隔小时执行一次,当前时间为time:{}", DateTimeToStr(new Date()));
    }

    /**
     * 每天8点40起床
     */
    @Scheduled(cron = "0 40 8 * * ?")
    public void getUp() {
        if (stopDay()) return;
        logger.info("*********:起床啦,time{}", DateTimeToStr(new Date()));
        mailService.sendImageMail(mailSendToUser, "起床啦", "懒猪起床啦", mailSendImagePath + Lists.newArrayList("起床啦.jpeg", "起床啦2.png").get(random.nextInt(2)));
    }

    /**
     * 每天9点25上班
     */
    @Scheduled(cron = "0 25 9 * * ?")
    public void goToWork() {
        if (stopDay()) return;
        logger.info("*********:上班啦,time{}", DateTimeToStr(new Date()));
        mailService.sendImageMail(mailSendToUser, "上班啦", "懒猪上班啦", mailSendImagePath + Lists.newArrayList("上班啦.jpeg", "上班啦2.png").get(random.nextInt(2)));
    }

    /**
     * 每天12点00午饭
     */
    @Scheduled(cron = "0 00 12 * * ?")
    public void lunch() {
        if (stopDay()) return;
        logger.info("*********:午饭啦,time{}", DateTimeToStr(new Date()));
        mailService.sendImageMail(mailSendToUser, "午饭啦", "懒猪午饭啦", mailSendImagePath + Lists.newArrayList("午饭啦2.png", "午饭啦.jpeg").get(random.nextInt(2)));
    }

    /**
     * 每天12点35午休
     */
    @Scheduled(cron = "0 35 12 * * ?")
    public void lunchBreak() {
        if (stopDay()) return;
        logger.info("*********:午休啦,time{}", DateTimeToStr(new Date()));
        mailService.sendImageMail(mailSendToUser, "午休啦", "懒猪午休啦", mailSendImagePath + Lists.newArrayList("午休啦2.png", "午休啦.jpeg").get(random.nextInt(2)));
    }

    /**
     * 每天18点00晚饭
     */
    @Scheduled(cron = "0 00 18 * * ?")
    public void dinner() {
        if (stopDay()) return;
        logger.info("*********:晚饭啦,time{}", DateTimeToStr(new Date()));
        mailService.sendImageMail(mailSendToUser, "晚饭啦", "懒猪晚饭啦", mailSendImagePath + Lists.newArrayList("晚饭啦2.png", "晚饭啦.jpeg").get(random.nextInt(2)));
    }

    /**
     * 每天20点00下班
     */
    @Scheduled(cron = "0 00 20 * * ?")
    public void offWork() {
        if (stopDay()) return;
        logger.info("*********:下班啦,time{}", DateTimeToStr(new Date()));
        mailService.sendImageMail(mailSendToUser, "下班啦", "懒猪下班啦", mailSendImagePath + Lists.newArrayList("下班啦2.png", "下班啦.jpeg").get(random.nextInt(2)));
    }

    /**
     * 每天23点00睡觉
     */
    @Scheduled(cron = "0 00 23 * * ?")
    public void goodNight() {
        logger.info("*********:睡觉啦,time{}", DateTimeToStr(new Date()));
        mailService.sendImageMail(mailSendToUser, "睡觉啦", "懒猪睡觉啦", mailSendImagePath + Lists.newArrayList("睡觉啦2.png", "睡觉啦.jpeg").get(random.nextInt(2)));
    }

    private boolean stopDay() {
        Date now = new Date();
        List<String> stopDays = JSON.parseArray(mailSendStopDays, String.class);
        return DateUtil.isWeekend(now) || (!CollectionUtils.isEmpty(stopDays) && stopDays.contains(DateUtil.DateToStr(now)));
    }
}
