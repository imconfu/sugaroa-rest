package com.sugaroa.rest.service;

import com.sugaroa.rest.domain.Role;
import com.sugaroa.rest.domain.Permission;
import com.sugaroa.rest.domain.Role;
import com.sugaroa.rest.domain.RoleRepository;
import com.sugaroa.rest.exception.AppException;
import org.springframework.beans.BeanWrapper;
import org.springframework.beans.BeanWrapperImpl;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
public class RoleService {

    @Resource
    private RoleRepository repository;

    public Role get(Integer id) {
        return repository.findOne(id);
    }

    public Map<Integer,String> getPairs() {
        Map<Integer, String> result = new HashMap<Integer, String>();

        List<Role> roles = repository.findByEnabledAndDeleted(1, 0);
        for (Role role : roles) {
            result.put(role.getId(), role.getTitle());
        }
        return result;
    }

    public List<Role> getAll() {
        List<Role> roles = repository.findByDeleted(0);
        return roles;
    }



    /**
     * 创建
     */
    public Role save(Map<String, Object> params) {
        Role role = new Role();
        return this.save(role, params);
    }

    /**
     * 根据Id更新
     *
     * @param id
     * @param params
     */
    public Role save(Integer id, Map<String, Object> params) {

        //先查找对应记录
        Role role = repository.findOne(id);
        return this.save(role, params);

    }

    public Role save(Role role, Map<String, Object> params) {

        //初始化BeanWrapper
        BeanWrapper bw = new BeanWrapperImpl(role);

        //根据params对需要更新的值做处理，即动态调用对应的setter方法
        for (Map.Entry<String, Object> entry : params.entrySet()) {
            ////有设置关联权限
            if (entry.getKey().equals("permissions")) {
                role.getPermissions().clear();
                List<Integer> permissions = (List<Integer>)entry.getValue();
                for(Integer id : permissions) {
                    role.getPermissions().add(new Permission(id));
                }
            } else {
                //只要有传参数进来，就认为修改该属性
                bw.setPropertyValue(entry.getKey(), entry.getValue());
            }
        }
        return repository.save(role);
    }
}
