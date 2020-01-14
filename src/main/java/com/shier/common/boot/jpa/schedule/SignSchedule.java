package com.shier.common.boot.jpa.schedule;

import com.alibaba.fastjson.JSONObject;
import com.google.common.collect.Lists;
import com.shier.common.boot.jpa.common.utils.DateUtil;
import com.shier.common.boot.jpa.common.utils.HttpUtil;
import com.shier.common.boot.jpa.service.MailService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
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
    private static final String imagePath = "/home/jpa/jpa/image/";
    /**
     * 发送邮箱
     */
    private static final String TO = "1819510836@qq.com";
    @Resource
    private MailService mailService;


    //每隔 5000 毫秒执行一次
    @Scheduled(fixedRate = 3600000)
    public void reportCurrentTime() {
        logger.info("每隔小时执行一次,当前时间为time:{}", DateTimeToStr(new Date()));
    }

    //每天凌晨5点签到
    @Scheduled(cron = "0 0 5 * * ?")
    public void fixTimeExecution() {
        logger.info("*********签到当前时间为time:{}", DateTimeToStr(new Date()));
        JSONObject body = new JSONObject();
        body.put("isSignIn", 1);
        body.put("origin", 4);
        Map<String, String> headers = new HashMap<>();
        headers.put("appId", "1000000");

        headers.put("token", "IGNKPppnd4GUFi9QVMHNYA==1567393206d2026b92efe5c668722177ed8de735a1");
        try {
            logger.info("******sign result:{}", HttpUtil.put("https://mapi.fulapay.com/fula/checkin/signIn", body.toJSONString(), headers));
        } catch (Exception e) {
            logger.info("******topic.sign Exception Msg:{}", e.getMessage());
        }
        logger.info("*********签到当前时间为time:{}", DateTimeToStr(new Date()));
    }

    /**
     * 每天8点40起床
     */
    @Scheduled(cron = "0 40 8 * * ?")
    public void getUp() {
        if (holiday()) return;
        logger.info("*********:起床啦,time{}", DateTimeToStr(new Date()));
        mailService.sendImageMail(TO, "起床啦", "懒猪起床啦", imagePath + Lists.newArrayList("起床啦.jpeg", "起床啦2.png").get(random.nextInt(2)));
    }

    /**
     * 每天9点25上班
     */
    @Scheduled(cron = "0 25 9 * * ?")
    public void goToWork() {
        if (holiday()) return;
        logger.info("*********:上班啦,time{}", DateTimeToStr(new Date()));
        mailService.sendImageMail(TO, "上班啦", "懒猪上班啦", imagePath + Lists.newArrayList("上班啦.jpeg", "上班啦2.png").get(random.nextInt(2)));
    }

    /**
     * 每天12点00午饭
     */
    @Scheduled(cron = "0 00 12 * * ?")
    public void lunch() {
        if (holiday()) return;
        logger.info("*********:午饭啦,time{}", DateTimeToStr(new Date()));
        mailService.sendImageMail(TO, "午饭啦", "懒猪午饭啦", imagePath + Lists.newArrayList("午饭啦2.png", "午饭啦.jpeg").get(random.nextInt(2)));
    }

    /**
     * 每天12点35午休
     */
    @Scheduled(cron = "0 35 12 * * ?")
    public void lunchBreak() {
        if (holiday()) return;
        logger.info("*********:午休啦,time{}", DateTimeToStr(new Date()));
        mailService.sendImageMail(TO, "午休啦", "懒猪午休啦", imagePath + Lists.newArrayList("午休啦2.png", "午休啦.jpeg").get(random.nextInt(2)));
    }

    /**
     * 每天18点00晚饭
     */
    @Scheduled(cron = "0 00 18 * * ?")
    public void dinner() {
        if (holiday()) return;
        logger.info("*********:晚饭啦,time{}", DateTimeToStr(new Date()));
        mailService.sendImageMail(TO, "晚饭啦", "懒猪晚饭啦", imagePath + Lists.newArrayList("晚饭啦2.png", "晚饭啦.jpeg").get(random.nextInt(2)));
    }

    /**
     * 每天20点00下班
     */
    @Scheduled(cron = "0 00 20 * * ?")
    public void offWork() {
        if (holiday()) return;
        logger.info("*********:下班啦,time{}", DateTimeToStr(new Date()));
        mailService.sendImageMail(TO, "下班啦", "懒猪下班啦", imagePath + Lists.newArrayList("下班啦2.png", "下班啦.jpeg").get(random.nextInt(2)));
    }

    /**
     * 每天23点00睡觉
     */
    @Scheduled(cron = "0 00 23 * * ?")
    public void goodNight() {
        logger.info("*********:睡觉啦,time{}", DateTimeToStr(new Date()));
        mailService.sendImageMail(TO, "睡觉啦", "懒猪睡觉啦", imagePath + Lists.newArrayList("睡觉啦2.png", "睡觉啦.jpeg").get(random.nextInt(2)));
    }

    private boolean holiday() {
        Date now = new Date();
        Date laborDayStart = DateUtil.getStartTimeOfDay(DateUtil.StrToDate("2019-10-01"));
        Date laborDayEnd = DateUtil.getEndTimeOfDay(DateUtil.StrToDate("2019-10-07"));
        return (DateUtil.isWeekend(now) || DateUtil.isDateBetween(laborDayStart, laborDayEnd, now));
    }
}
