package com.shier.common.boot.jpa.dao;

import com.shier.common.boot.jpa.model.JpaUser;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;


/**
 * @Author: liyunbiao
 * @Date: 2019/7/11 8:44 PM
 */
@Repository
public interface JpaUserDao extends JpaRepository<JpaUser, Long> {
    JpaUser findByMobile(String account);

    JpaUser findByMobileAndPwd(String account, String pwd);

    List<JpaUser> findByMobileOrMobile(String account1, String account2);

    @Query("SELECT o FROM JpaUser o WHERE o.mobile = :mobile1  OR o.mobile = :mobile2 ")
    List<JpaUser> findTwoMobile(@Param("mobile1") String mobile1, @Param("mobile2") String mobile2);

    @Query(nativeQuery = true, value = "SELECT * FROM user WHERE mobile = :mobile1  OR mobile = :mobile2 ")
    List<JpaUser> findSQL(@Param("mobile1") String mobile1, @Param("mobile2") String mobile2);

    @Query(value = "SELECT * FROM User WHERE id > ?1", countQuery = "SELECT count(*) FROM User WHERE id > ?1", nativeQuery = true)
    Page<JpaUser> findPageByIdAfter(Long id, Pageable pageable);

}
