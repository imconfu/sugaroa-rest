package com.sugaroa.rest.domain;

import org.springframework.beans.factory.annotation.Autowired;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import java.util.List;

/**
 * 实现自定义的方法
 */
public class UserRepositoryImpl implements UserRepositoryCustom{
    @Autowired
    @PersistenceContext
    private EntityManager entityManager;

    @Override
    public void testAA() {
        List<Object[]> users = entityManager.createNativeQuery("select * from oa_user").getResultList();
        for (Object[] objs : users) {
            System.out.print("location 1:" + objs[0]);
            System.out.print("location 2:" + objs[1]);
            System.out.print("location 3:" + objs[2]);
        }
    }
}
