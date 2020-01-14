package com.shier.common.boot.jpa.user;

import com.shier.common.boot.jpa.dao.JpaUserDao;
import com.shier.common.boot.jpa.model.JpaUser;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

/**
 * @Author: liyunbiao
 * @Date: 2019/7/11 8:48 PM
 */
@RunWith(SpringRunner.class)
@SpringBootTest
public class UserDaoTest {
    @Autowired
    private JpaUserDao jpaUserDao;

    @Before
    public void before() {
        JpaUser jpaUserDO = new JpaUser();
        jpaUserDO.setId(1L);
        jpaUserDO.setName("风清扬");
        jpaUserDO.setMobile("fengqy");
        jpaUserDO.setPwd("123456");
        jpaUserDao.save(jpaUserDO);
        jpaUserDO = new JpaUser();
        jpaUserDO.setId(3L);
        jpaUserDO.setName("东方不败");
        jpaUserDO.setMobile("bubai");
        jpaUserDO.setPwd("123456");
        jpaUserDao.save(jpaUserDO);
        jpaUserDO.setId(5L);
        jpaUserDO.setName("向问天");
        jpaUserDO.setMobile("wentian");
        jpaUserDO.setPwd("123456");
        jpaUserDao.save(jpaUserDO);
    }
    @Test
    public void testAdd() {
        JpaUser jpaUserDO = new JpaUser();
        jpaUserDO.setId(2L);
        jpaUserDO.setName("任我行");
        jpaUserDO.setMobile("renwox");
        jpaUserDO.setPwd("123456");
        jpaUserDao.save(jpaUserDO);
        jpaUserDO = new JpaUser();
        jpaUserDO.setId(4L);
        jpaUserDO.setName("令狐冲");
        jpaUserDO.setMobile("linghuc");
        jpaUserDO.setPwd("123456");
        jpaUserDao.save(jpaUserDO);
    }

    @After
    public void after() {
        jpaUserDao.deleteById(1L);
        jpaUserDao.deleteById(3L);
        jpaUserDao.deleteById(5L);
    }
}
