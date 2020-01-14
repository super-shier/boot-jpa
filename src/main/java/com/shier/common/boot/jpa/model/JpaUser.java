package com.shier.common.boot.jpa.model;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.ToString;

import javax.persistence.*;
import java.io.Serializable;
import java.util.Date;

/**
 * @Author: liyunbiao
 * @Date: 2019/7/11 5:31 PM
 */
@Data
@ToString
@Entity
@Table(name = "jpa_user")
@org.hibernate.annotations.Table(appliesTo = "jpa_user", comment = "用户表")
public class JpaUser implements Serializable {
    /**
     * 用户id
     */
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @ApiModelProperty("用户id")
    private Long id;
    /**
     * 用户姓名
     */
    @Column(name = "name", columnDefinition = "varchar(32) COMMENT '用户姓名'")
    @ApiModelProperty("用户姓名")
    private String name;
    /**
     * 手机号
     */
    @Column(name = "mobile", columnDefinition = "varchar(11) COMMENT '手机号'")
    @ApiModelProperty("手机号")
    private String mobile;
    /**
     * 密码
     */
    @Column(name = "pwd", columnDefinition = "varchar(256) COMMENT '密码'")
    @ApiModelProperty("密码")
    private String pwd;
    /**
     * 性别
     */
    @Column(columnDefinition = "int(2) default 0 comment '性别 0:男 1:女'")
    @ApiModelProperty("性别")
    private int sex;
    /**
     * 创建时间
     */
    @Column(name = "createTime", columnDefinition = "DATETIME COMMENT '创建时间'")
    @ApiModelProperty("创建时间")
    private Date createTime;
    /**
     * 修改时间
     */
    @Column(name = "modifyTime", columnDefinition = "DATETIME COMMENT '修改时间'")
    @ApiModelProperty("修改时间")
    private Date modifyTime;
}
