package com.shier.common.boot.jpa.service;

import com.shier.common.boot.jpa.model.JpaUser;
import org.springframework.data.domain.Page;

import java.util.List;

/**
 * @Author: liyunbiao
 * @Date: 2019/7/19 5:44 PM
 * @description
 */
public interface JpaUserService {
    /**
     * 保存用户
     *
     * @param jpaUser 用户信息对象
     * @return 保存成功后的用户信息或null
     */
    JpaUser addUser(JpaUser jpaUser);

    /**
     * 更新用户
     *
     * @param jpaUser 用户信息对象
     * @return 修改成功后的用户信息或null
     */
    JpaUser updateUser(JpaUser jpaUser);

    /**
     * 删除用户
     *
     * @param id 用户id
     */
    void deleteUser(Long id);

    /**
     * 查询用户信息
     *
     * @param id 用户ID
     * @return 用户信息或null
     */

    JpaUser selectById(Long id);

    /**
     * 查询用户信息
     *
     * @param mobile 用户手机号
     * @return 用户信息或null
     */
    JpaUser selectByMobile(String mobile);

    /**
     * 用户列表
     *
     * @return 返回用户对象列表
     */
    List<JpaUser> selectAll();

    /**
     * 条件分页查询
     *
     * @param page    页数
     * @param size    每页多少条
     * @param sort    排序
     * @param jpaUser 查询用户信息
     * @return 分页结果
     */
    Page findJpaUserPageWithCondition(Integer page, Integer size, String sort, JpaUser jpaUser);
}
