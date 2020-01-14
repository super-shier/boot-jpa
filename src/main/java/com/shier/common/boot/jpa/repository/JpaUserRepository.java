package com.shier.common.boot.jpa.repository;

import com.shier.common.boot.jpa.model.JpaUser;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.PagingAndSortingRepository;
import org.springframework.stereotype.Repository;

/**
 * @Author: liyunbiao
 * @Date: 2019/7/22 4:31 PM
 * @description
 */
@Repository("jpaUserRepository")
public interface JpaUserRepository extends CrudRepository<JpaUser, Long>, JpaSpecificationExecutor,
        PagingAndSortingRepository<JpaUser, Long> {
}
