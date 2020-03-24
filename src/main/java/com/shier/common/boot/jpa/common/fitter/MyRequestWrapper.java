package com.shier.common.boot.jpa.common.fitter;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.shier.common.boot.jpa.common.utils.AbstractRequestUtils;

import javax.servlet.ReadListener;
import javax.servlet.ServletInputStream;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletRequestWrapper;
import java.io.BufferedReader;
import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.HashMap;
import java.util.Map;
import java.util.Objects;

/**
 * @Author: liyunbiao
 * @Date: 2019/7/25 6:25 PM
 * @description
 */
public class MyRequestWrapper extends HttpServletRequestWrapper {

    private final String body;

    private Map<String, String[]> params = new HashMap<>();

    public MyRequestWrapper(HttpServletRequest request) throws IOException {
        super(request);
        JSONObject requestBody = JSON.parseObject(AbstractRequestUtils.getRequestBody(request));
        if (Objects.nonNull(requestBody)) {
            requestBody.put("department", "department-body");
        }
        Map<String, String[]> requestParams = new HashMap<>(request.getParameterMap());
        requestParams.put("department", new String[]{"department-param"});
        request.setAttribute("department", "department-attribute");
        this.body = Objects.nonNull(requestBody) ? requestBody.toJSONString() : null;
        this.params.putAll(requestParams);
    }

    public String getBody() {
        return body;
    }


    @Override
    public ServletInputStream getInputStream() {
        final ByteArrayInputStream bais = new ByteArrayInputStream(body.getBytes());
        return new ServletInputStream() {

            @Override
            public boolean isFinished() {
                return false;
            }

            @Override
            public boolean isReady() {
                return false;
            }

            @Override
            public void setReadListener(ReadListener readListener) {
            }

            @Override
            public int read() {
                return bais.read();
            }
        };
    }

    @Override
    public BufferedReader getReader() {
        return new BufferedReader(new InputStreamReader(this.getInputStream()));
    }

    @Override
    public String getParameter(String name) {
        //重写getParameter，代表参数从当前类中的map获取
        String[] values = params.get(name);
        if (values == null || values.length == 0) {
            return null;
        }
        return values[0];
    }

    @Override
    public String[] getParameterValues(String name) {//同上
        return params.get(name);
    }

}
