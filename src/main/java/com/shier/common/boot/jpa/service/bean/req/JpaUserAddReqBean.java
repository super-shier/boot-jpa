package com.shier.common.boot.jpa.service.bean.req;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.ToString;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import java.io.Serializable;

/**
 * @Author: liyunbiao
 * @Date: 2019/7/23 11:29 AM
 * @description
 */
@Data
@ToString
@ApiModel(value = "添加用户对象")
public class JpaUserAddReqBean implements Serializable {

    /**
     * 用户名
     */
    @ApiModelProperty(value = "用户名", required = true)
    @NotNull(message = "用户名不能为空")
    private String name;
    /**
     * 手机号
     */
    @ApiModelProperty(value = "手机号", required = true)
    @NotNull(message = "手机号不能为空")
    private String mobile;
    /**
     * 密码
     */
    @ApiModelProperty(value = "密码", required = true)
    @NotBlank(message = "密码不能为空")
    private String pwd;
    /**
     * 性别
     */
    @ApiModelProperty(value = "性别")
    private int sex;
}
