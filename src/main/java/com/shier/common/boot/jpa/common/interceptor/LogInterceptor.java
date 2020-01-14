package com.shier.common.boot.jpa.common.interceptor;

import com.alibaba.fastjson.JSON;
import com.shier.common.boot.jpa.common.utils.AbstractRequestUtils;
import com.shier.common.boot.jpa.common.utils.IpHelper;
import com.shier.common.boot.jpa.common.utils.MapToolsUtil;
import com.shier.common.boot.jpa.model.JpaAtlasModel;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.handler.HandlerInterceptorAdapter;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.Objects;

/**
 * @Author: liyunbiao
 * @Date: 2019/7/24 12:37 PM
 * @description
 */
@Component
public class LogInterceptor extends HandlerInterceptorAdapter {
    private static org.slf4j.Logger logger = LoggerFactory.getLogger(LogInterceptor.class);

    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response,
                             Object handler) {
        request.setAttribute("REQUEST_START_TIME", System.currentTimeMillis());
        JpaAtlasModel atlasModel = MapToolsUtil.getAddressByIp(IpHelper.getIpAddr(request));
        if (logger.isInfoEnabled()) {
            final StringBuilder builder = new StringBuilder();
            builder.append("@[");
            builder.append(request.getRequestURI()).append(":");
            builder.append(request.getMethod()).append(":");
            builder.append(IpHelper.getIpAddr(request)).append(":");
            builder.append(Objects.nonNull(atlasModel) ? atlasModel.getAddress() : "地址未知");
            builder.append("] @Request params >>");
            builder.append(AbstractRequestUtils.getRequestBody(request));
            request.getParameterMap().forEach((k, v) ->
                    builder.append(" -").append(k).append(":")
                            .append(JSON.toJSONString(v)));
            logger.info(builder.toString());
        }

        return true;
    }

    @Override
    public void postHandle(HttpServletRequest request, HttpServletResponse response, Object handler,
                           ModelAndView modelAndView) {
        logger.info("此次请求共消耗了{}毫秒", (System.currentTimeMillis()
                - Long.parseLong(request.getAttribute("REQUEST_START_TIME").toString())));
    }

}
