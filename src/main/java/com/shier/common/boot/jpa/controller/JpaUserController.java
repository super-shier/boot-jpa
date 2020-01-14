package com.shier.common.boot.jpa.controller;

import com.shier.common.boot.jpa.common.config.ApiResponse;
import com.shier.common.boot.jpa.model.JpaUser;
import com.shier.common.boot.jpa.service.JpaUserService;
import com.shier.common.boot.jpa.service.bean.req.JpaUserAddReqBean;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.BeanUtils;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;
import javax.validation.Valid;

/**
 * @Author: liyunbiao
 * @Date: 2019/7/23 10:55 AM
 * @description
 */
@RestController
@RequestMapping(value = "/user")
@Api(value = "用户相关接口类", tags = {"用户相关接口类"})
public class JpaUserController extends BaseController {
    private static final Logger logger = LoggerFactory.getLogger(JpaUserController.class);
    @Resource
    private JpaUserService jpaUserService;

    @PostMapping(value = "add")
    @ApiOperation(value = "添加用户信息")
    public ApiResponse<JpaUser> addUser(@ApiParam(value = "用户信息", required = true) @Valid @RequestBody JpaUserAddReqBean jpaUserAddReqBean) {
        logger.info("********添加用户user:{}", jpaUserAddReqBean.toString());
        JpaUser jpaUser = new JpaUser();
        BeanUtils.copyProperties(jpaUserAddReqBean, jpaUser);
        jpaUserService.addUser(jpaUser);
        return buildResponse(jpaUser);
    }
}
