package com.sugaroa.rest.service;

import com.sugaroa.rest.domain.Role;
import com.sugaroa.rest.domain.User;
import com.sugaroa.rest.domain.User;
import com.sugaroa.rest.domain.UserRepository;
import com.sugaroa.rest.exception.AppException;
import org.apache.shiro.crypto.RandomNumberGenerator;
import org.apache.shiro.crypto.SecureRandomNumberGenerator;
import org.apache.shiro.crypto.hash.SimpleHash;
import org.apache.shiro.util.ByteSource;
import org.springframework.beans.BeanWrapper;
import org.springframework.beans.BeanWrapperImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.*;

@Service
public class UserService {

    private final UserRepository repository;

    /**
     * shiro
     *
     * @param account
     * @return
     */
    public User findByUsername(String account) {
        System.out.println("UserService.findByAccount()");
        return repository.findByAccount(account);
    }

    @Autowired
    public UserService(UserRepository repository) {
        this.repository = repository;
        ;
    }


    /**
     * 创建
     */
    public User save(Map<String, Object> params) {
        User user = new User();
        return this.save(user, params);
    }

    /**
     * 根据Id更新
     *
     * @param id
     * @param params
     */
    public User save(Integer id, Map<String, Object> params) {

        //先查找对应记录
        User user = repository.findOne(id);
        return this.save(user, params);

    }

    public User save(User user, Map<String, Object> params) {
        //password需要特殊处理
        String password = (String) params.getOrDefault("password", "");

        //初始化BeanWrapper
        BeanWrapper bw = new BeanWrapperImpl(user);

        //根据params对需要更新的值做处理，即动态调用对应的setter方法
        for (Map.Entry<String, Object> entry : params.entrySet()) {
            if (entry.getKey().equals("roles")) {
                List<Integer> roles = (List<Integer>) entry.getValue();
                if (roles.size() > 0) {
                    if (roles != null) {
                        user.getRoles().clear();
                    }
                    for (Integer id : roles) {
                        user.getRoles().add(new Role(id));
                    }
                }
            } else if (entry.getKey().equals("password")) {
                continue;
            } else {
                //只要有传参数进来，就认为修改该属性
                bw.setPropertyValue(entry.getKey(), entry.getValue());
            }
        }
        //把实体类的值填充了，才能再做下一步处理。

        // 对password特殊处理
        if (!password.isEmpty()) {
            RandomNumberGenerator randomNumberGenerator = new SecureRandomNumberGenerator();
            String salt = randomNumberGenerator.nextBytes().toHex();
            user.setSalt(salt);

            //结果为：md5((username+salt)+password)
            String saltPassword = new SimpleHash("MD5", password, ByteSource.Util.bytes(user.getCredentialsSalt()), 1).toHex();
            user.setPassword(saltPassword);

        }
        return repository.save(user);
    }

    public User get(Integer id) {
        return repository.findOne(id);
    }
}
