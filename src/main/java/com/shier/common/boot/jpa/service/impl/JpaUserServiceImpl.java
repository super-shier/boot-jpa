package com.shier.common.boot.jpa.service.impl;

import com.shier.common.boot.jpa.dao.JpaUserDao;
import com.shier.common.boot.jpa.model.JpaUser;
import com.shier.common.boot.jpa.repository.JpaUserRepository;
import com.shier.common.boot.jpa.service.JpaUserService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import javax.annotation.Resource;
import javax.persistence.criteria.Predicate;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

/**
 * @Author: liyunbiao
 * @Date: 2019/7/22 4:20 PM
 * @description 用户信息操作
 */
@Service
public class JpaUserServiceImpl implements JpaUserService {
    private static final Logger logger = LoggerFactory.getLogger(JpaUserServiceImpl.class);
    @Autowired
    private JpaUserDao jpaUserDao;
    @Resource
    JpaUserRepository jpaUserRepository;

    @Override
    public JpaUser addUser(JpaUser jpaUser) {
        return jpaUserDao.save(jpaUser);
    }

    @Override
    public JpaUser updateUser(JpaUser jpaUser) {
        return jpaUserDao.saveAndFlush(jpaUser);
    }

    @Override
    public void deleteUser(Long id) {
        jpaUserDao.deleteById(id);
    }

    @Override
    public JpaUser selectById(Long id) {
        return jpaUserDao.findById(id).get();
    }

    @Override
    public JpaUser selectByMobile(String mobile) {
        return jpaUserDao.findByMobile(mobile);
    }

    @Override
    public List<JpaUser> selectAll() {
        return jpaUserDao.findAll();
    }

    @Override
    public Page<JpaUser> findJpaUserPageWithCondition(Integer page, Integer size, String sort, JpaUser jpaUser) {
        Specification<JpaUser> specification = (root, query, cb) -> {
            List<Predicate> predicateList = new ArrayList<>();
            // 创建 Predicate
            if (!StringUtils.isEmpty(jpaUser.getId())) {
                Predicate predicate = cb.conjunction();
                predicate.getExpressions().add(cb.equal(root.get("id"), jpaUser.getId()));
                predicateList.add(predicate);
            }
            if (!StringUtils.isEmpty(jpaUser.getMobile())) {
                Predicate predicate = cb.conjunction();
                predicate.getExpressions().add(cb.equal(root.get("mobile"), jpaUser.getMobile()));
                predicateList.add(predicate);
            }
            if (!StringUtils.isEmpty(jpaUser.getName())) {
                predicateList.add(cb.like(root.get("name"), "%" + jpaUser.getName() + "%"));
            }
            if (!StringUtils.isEmpty(jpaUser.getPwd())) {
                predicateList.add(cb.like(root.get("pwd"), "%" + jpaUser.getPwd() + "%"));
            }
            if (Objects.nonNull(jpaUser.getCreateTime())) {
                predicateList.add(cb.greaterThan(root.get("createTime"), jpaUser.getCreateTime()));
            }
            //in 查询
            //predicateList.add(cb.in(root.get("sex")).value(Lists.newArrayList(0, 1)));
            return cb.and(predicateList.toArray(new Predicate[0]));
        };
        return jpaUserRepository.findAll(specification, PageRequest.of(page, size, Sort.Direction.ASC, sort));
    }
}
