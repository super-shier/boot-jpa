package com.shier.common.boot.jpa.controller;

import com.luhuiguo.chinese.ChineseUtils;
import com.luhuiguo.chinese.pinyin.PinyinFormat;
import com.shier.common.boot.jpa.common.config.ApiResponse;
import com.shier.common.boot.jpa.common.config.RateLimit;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

/**
 * @Author: liyunbiao
 * @Date: 2019/7/17 2:32 PM
 * @description
 */
@RestController
@RequestMapping(value = "/index")
@Api(value = "hello页面有关接口类", tags = {"hello页面有关接口类"})
public class JpaIndexController extends BaseController {
    private static final Logger logger = LoggerFactory.getLogger(JpaIndexController.class);

    @GetMapping(value = "/hello")
    @ApiOperation(value = "hello页面")
    public ApiResponse index() {
        String name = ChineseUtils.toPinyin("李云标", PinyinFormat.ABBR_PINYIN_FORMAT);
        logger.info("**********hello页面name:{}", name);
        return buildResponse("hello页面");
    }

    @GetMapping(value = "/welcome")
    @ApiOperation(value = "welcome页面")
    public String welcome(@RequestParam(value = "用户姓名") String name) {
        logger.info("**********welcome页面 name:{}", name);
        return "welcome页面";
    }

    @GetMapping(value = "/rateLimiter")
    @RateLimit(perSecond = 1.0, timeOut = 500)
    public ApiResponse rateLimiter() {
        return buildResponse("get token success");
    }
}