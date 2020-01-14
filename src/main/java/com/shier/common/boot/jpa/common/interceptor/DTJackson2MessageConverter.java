package com.shier.common.boot.jpa.common.interceptor;

import com.alibaba.fastjson.JSON;
import com.fasterxml.jackson.core.JsonGenerator;
import org.springframework.http.HttpOutputMessage;
import org.springframework.http.converter.json.Jackson2ObjectMapperBuilder;
import org.springframework.http.converter.json.MappingJackson2HttpMessageConverter;
import org.springframework.http.converter.json.MappingJacksonValue;

import java.io.IOException;
import java.lang.reflect.Type;
import java.util.TimeZone;

/**
 * @Author: liyunbiao
 * @Date: 2019/7/23 12:37 PM
 * @description
 */
public class DTJackson2MessageConverter extends MappingJackson2HttpMessageConverter {
    public DTJackson2MessageConverter() {
        super(Jackson2ObjectMapperBuilder.json().timeZone(TimeZone.getTimeZone("GMT+8")).simpleDateFormat("yyyy-MM-dd HH:mm:ss")
                .featuresToEnable(JsonGenerator.Feature.WRITE_BIGDECIMAL_AS_PLAIN)
                .build());
    }

    @Override
    protected void writeInternal(Object object, Type type, HttpOutputMessage outputMessage)
            throws IOException {
        super.writeInternal(object, type, outputMessage);

        logger.info("@Response body >> "
                + (object instanceof MappingJacksonValue ? JSON
                .toJSONString(((MappingJacksonValue) object).getValue()) : JSON
                .toJSONString(object)));
    }
}
