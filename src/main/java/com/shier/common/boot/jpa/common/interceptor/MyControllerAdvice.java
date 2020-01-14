package com.shier.common.boot.jpa.common.interceptor;

import com.shier.common.boot.jpa.common.config.ApiResponse;
import com.shier.common.boot.jpa.common.enums.ErrorCode;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.support.DefaultMessageSourceResolvable;
import org.springframework.core.annotation.Order;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindException;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.context.request.WebRequest;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;

import javax.naming.ServiceUnavailableException;
import java.util.HashMap;
import java.util.Map;
import java.util.Objects;

import static org.springframework.http.HttpStatus.*;

/**
 * @Author: liyunbiao
 * @Date: 2019/7/24 3:58 PM
 * @description 异常统一处理
 */
@Order
@ControllerAdvice
public class MyControllerAdvice extends ResponseEntityExceptionHandler {
    private static final String SYSTEM_EXCEPTION_CODE = "5000";
    private static final String VALID_EXCEPTION_CODE = "4000";
    private static final Map<String, String> ERROR_DATA = new HashMap<String, String>() {{
        put(SYSTEM_EXCEPTION_CODE, "系统错误");
        put(VALID_EXCEPTION_CODE, "信息验证有误");
    }};

    private static final Logger logg = LoggerFactory.getLogger(MyControllerAdvice.class);


    /**
     * 参数校验异常
     */
    @Override
    protected ResponseEntity<Object> handleMethodArgumentNotValid(MethodArgumentNotValidException ex,
                                                                  HttpHeaders headers, HttpStatus status,
                                                                  WebRequest request) {
        String errorMessage = ex.getBindingResult().getFieldErrors().stream()
                .map(DefaultMessageSourceResolvable::getDefaultMessage)
                .findFirst()
                .orElse(ex.getMessage());
        return super.handleExceptionInternal(ex, buildResponse(VALID_EXCEPTION_CODE, errorMessage), null, OK, request);
    }


    /**
     * 服务不可用
     *
     * @param ex      {@link ServiceUnavailableException}
     * @param request {@link WebRequest}
     * @return {@link ResponseEntity}
     */
    @ExceptionHandler(ServiceUnavailableException.class)
    public ResponseEntity<Object> handleBizTimeoutException(ServiceUnavailableException ex,
                                                            WebRequest request) {
        return this.handleExceptionInternal(ex, SERVICE_UNAVAILABLE, request);
    }


    /**
     * 处理默认异常
     *
     * @param ex      {@link Exception}
     * @param request {@link WebRequest}
     * @return {@link ResponseEntity}
     */
    @ExceptionHandler(value = {Exception.class})
    public ResponseEntity<Object> handleOtherException(Exception ex, WebRequest request) {
        return this.handleExceptionInternal(ex, INTERNAL_SERVER_ERROR, request);
    }

    /**
     * 处理500错误
     *
     * @param ex      异常
     * @param status  异常状态
     * @param request 请求
     * @return {@link ResponseEntity}
     */
    private ResponseEntity<Object> handleExceptionInternal(Exception ex, HttpStatus status,
                                                           WebRequest request) {
        logger.error(ex.getMessage(), ex);
        return super.handleExceptionInternal(ex, buildResponse(ex), null, status, request);
    }

    private ApiResponse buildResponse(Exception ex) {
        String errorMsg = null;
        if (ex instanceof BindException) {
            BindException e = (BindException) ex;
            if (e.hasFieldErrors()) {
                errorMsg = Objects.requireNonNull(e.getFieldError()).getDefaultMessage();
            }
        } else {
            errorMsg = ERROR_DATA.get(SYSTEM_EXCEPTION_CODE);
        }
        return buildResponse(SYSTEM_EXCEPTION_CODE, errorMsg);
    }

    private ApiResponse buildResponse(String code, String errorMsg) {
        ErrorCode enumErrCode = ErrorCode.find(String.valueOf(code));
        if (Objects.nonNull(enumErrCode)) {
            return new ApiResponse(enumErrCode,null);
        } else {
            return new ApiResponse(String.valueOf(code), errorMsg);
        }
    }

}
