package com.shier.common.boot.jpa.user;

import com.shier.common.boot.jpa.model.JpaUser;
import com.shier.common.boot.jpa.service.JpaUserService;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.domain.Page;
import org.springframework.test.context.junit4.SpringRunner;

import javax.annotation.Resource;
import java.util.Date;
import java.util.List;
import java.util.Objects;

/**
 * @Author: liyunbiao
 * @Date: 2019/7/11 8:53 PM
 */
@RunWith(SpringRunner.class)
@SpringBootTest
public class UserTest {
    private static final Logger logger = LoggerFactory.getLogger(UserTest.class);
    @Resource
    private JpaUserService jpaUserService;

    @Test
    public void save() {
        JpaUser jpaUserDO;
        for (long i = 0; i < 100; i++) {
            jpaUserDO = new JpaUser();
            jpaUserDO.setName("shier" + i + 100);
            jpaUserDO.setMobile("18658161" + (i + 100));
            jpaUserDO.setPwd("12345" + i + 100);
            jpaUserDO.setCreateTime(new Date());
            jpaUserDO.setModifyTime(new Date());
            jpaUserService.addUser(jpaUserDO);
        }

    }

    @Test
    public void findById() {
        JpaUser jpaUserDO = jpaUserService.selectById(100L);
        if (Objects.nonNull(jpaUserDO)) {
            logger.info("**********name:{},account:{}", jpaUserDO.getName(), jpaUserDO.getMobile());
        }
    }

    @Test
    public void findAll() {
        List<JpaUser> jpaUserDOList = jpaUserService.selectAll();
        for (JpaUser jpaUserDO : jpaUserDOList) {
            logger.info("**********:name{},account:{}", jpaUserDO.getName(), jpaUserDO.getMobile());
        }
    }

    @Test
    public void fndByAccount() {
        JpaUser jpaUserDO = jpaUserService.selectByMobile("18658161116");
        if (jpaUserDO != null) {
            logger.info("**********id:{},name:{},account:{}", jpaUserDO.getId(), jpaUserDO.getName(), jpaUserDO.getMobile());
        }
    }


    @Test
    public void selectPageCondition() {
        JpaUser jpaUser = new JpaUser();
        jpaUser.setId(120l);
        jpaUser.setMobile("18658161120");
        jpaUser.setName("shier");
        jpaUser.setPwd("12345");
        jpaUser.setCreateTime(new Date());
        Page<JpaUser> jpaUserPage = jpaUserService.findJpaUserPageWithCondition(0, 100, "id", jpaUser);
        logger.info("********totalPage:{},totalSize:{}", jpaUserPage.getTotalPages(), jpaUserPage.getTotalElements());
        jpaUserPage.stream().forEach(user -> {
            logger.info("********jpaUser:{}", user.toString());
        });
    }

    @Test
    public void update() {
        JpaUser jpaUser = jpaUserService.selectById(100L);
        if (Objects.nonNull(jpaUser)) {
            logger.info("********jpaUser:{}", jpaUser.toString());
            jpaUser.setName("李云标");
            jpaUser.setPwd("123456");
            jpaUser.setMobile("18658161306");
            jpaUser = jpaUserService.updateUser(jpaUser);
            logger.info("********jpaUser:{}", jpaUser.toString());
        }
    }
}
