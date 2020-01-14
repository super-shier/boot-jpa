package com.shier.common.boot.jpa;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.context.web.WebAppConfiguration;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.context.WebApplicationContext;

/**
 * @Author: liyunbiao
 * @Date: 2019/8/31 10:17 AM
 * @description 测试基础类
 */
@RunWith(SpringRunner.class)
@SpringBootTest(classes = JpaApplication.class)
@WebAppConfiguration
public class BaseTest {
    protected Logger logger = LoggerFactory.getLogger(BaseTest.class);

    @Autowired
    private WebApplicationContext wac;

    private MockMvc mockMvc;

    @Before
    public void init() {
        mockMvc = MockMvcBuilders.webAppContextSetup(this.wac).build();
    }

    @Test
    public void sonarTest() {
        System.out.println("为了解决sonar 扫描加一个Test");
    }
}

