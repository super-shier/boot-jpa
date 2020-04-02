package com.shier.common.boot.jpa.common.fitter;

import com.shier.common.boot.jpa.common.utils.AbstractRequestUtils;

import javax.servlet.ReadListener;
import javax.servlet.ServletInputStream;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletRequestWrapper;
import java.io.BufferedReader;
import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStreamReader;

/**
 * @Author: liyunbiao
 * @Date: 2019/7/25 6:25 PM
 * @description
 */
public class MyRequestWrapper extends HttpServletRequestWrapper {

    private final String body;


    public MyRequestWrapper(HttpServletRequest request) throws IOException {
        super(request);
        this.body = AbstractRequestUtils.getRequestBody(request);
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


}
