package com.shier.common.boot.jpa.common.config;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.shier.common.boot.jpa.common.enums.ErrorCode;
import com.shier.common.boot.jpa.common.utils.MapToolsUtil;
import com.shier.common.boot.jpa.model.JpaAtlasModel;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.io.Serializable;

/**
 * @Author: liyunbiao
 * @Date: 2019/7/24 4:11 PM
 * @description
 */
@Data
@ApiModel(value = "请求响应")
@JsonInclude(JsonInclude.Include.NON_NULL)
public class ApiResponse<T> implements Serializable {
    @ApiModelProperty(value = "请求状态")
    private boolean success;
    @ApiModelProperty(value = "响应交易码")
    private String errorCode;
    @ApiModelProperty(value = "响应交易")
    private String errorMsg;
    @ApiModelProperty(value = "访问地址")
    private JpaAtlasModel atlasModel;
    @ApiModelProperty(value = "响应数据")
    private T data;

    public ApiResponse(String errorCode, String errorMsg, String ip) {
        this.errorCode = errorCode;
        this.errorMsg = errorMsg;
        this.atlasModel = MapToolsUtil.getAddressByIp(ip);
    }

    public ApiResponse(T data, String ip) {
        this.success = true;
        this.data = data;
        this.atlasModel = MapToolsUtil.getAddressByIp(ip);
    }

    public ApiResponse(ErrorCode errorCode, String ip) {
        this.errorCode = errorCode.getCode();
        this.errorMsg = errorCode.getMsg();
        this.atlasModel = MapToolsUtil.getAddressByIp(ip);
    }
}
