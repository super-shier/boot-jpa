package com.shier.common.boot.jpa.controller;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.serializer.SerializerFeature;
import com.shier.common.boot.jpa.common.config.ApiResponse;
import com.shier.common.boot.jpa.common.enums.ErrorCode;
import com.shier.common.boot.jpa.common.utils.IpHelper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.servlet.HandlerInterceptor;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

public class BaseController implements HandlerInterceptor {
    @Autowired
    private HttpServletRequest request;

    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {
        //System.out.println("======处理请求之前======");
        return true;
    }

    @Override
    public void postHandle(
            HttpServletRequest request, HttpServletResponse response, Object handler, ModelAndView modelAndView) throws Exception {
        //System.out.println("========处理请求后，渲染页面前======");
    }

    @Override
    public void afterCompletion(
            HttpServletRequest request, HttpServletResponse response, Object handler, Exception ex)
            throws Exception {
        //System.out.println("========视图渲染结束了，请求处理完毕====");
    }

    protected ApiResponse buildResponse(Object data) {
        return new ApiResponse(data, IpHelper.getIpAddr(request));
    }

    protected ApiResponse buildResponse(String errorCode, String errorMsg) {
        return new ApiResponse(errorCode, errorMsg, IpHelper.getIpAddr(request));
    }

    protected ApiResponse buildResponse(ErrorCode errorCode) {
        return new ApiResponse(errorCode, IpHelper.getIpAddr(request));
    }

    /**
     * 返回 JSON 格式对象
     *
     * @param object 转换对象
     * @return
     */
    private String toJson(Object object) {
        return JSON.toJSONString(object, SerializerFeature.BrowserCompatible);
    }

    /**
     * 返回 JSON 格式对象
     *
     * @param object 转换对象
     * @return
     */
    protected String toJson(Object object, String format) {
        if (format == null) {
            return toJson(object);
        }
        return JSON.toJSONStringWithDateFormat(object, format, SerializerFeature.WriteDateUseDateFormat);
    }

    protected String booleanToString(boolean rlt) {
        return rlt ? "true" : "false";
    }

}
