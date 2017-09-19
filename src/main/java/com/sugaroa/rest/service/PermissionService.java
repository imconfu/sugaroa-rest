package com.sugaroa.rest.service;

import com.sugaroa.rest.domain.*;
import com.sugaroa.rest.exception.AppException;
import org.springframework.beans.BeanWrapper;
import org.springframework.beans.BeanWrapperImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.*;

@Service
public class PermissionService {

    @Autowired
    private PermissionRepository repository;

    public Map<Integer,String> getPairs() {

        Map<Integer, String> pairs = new HashMap<Integer, String>();

        List<Permission> permissions = repository.findByEnabledAndDeleted(1, 0);
        for (SimpleTree permission : permissions) {
            pairs.put(permission.getId(), permission.getText());
        }
        return pairs;
    }

    public List<SimpleTree> getComboTree() {

        List<Permission> permissions = repository.findByEnabledAndDeleted(1, 0);

        List<SimpleTree> tree = new ArrayList<SimpleTree>();
        for (SimpleTree node1 : permissions) {
            boolean mark = false;
            for (SimpleTree node2 : permissions) {
                if (node1.getParentId() != null && node1.getParentId().equals(node2.getId())) {
                    mark = true;
                    if (node2.getChildren() == null)
                        node2.setChildren(new ArrayList<Object>());
                    node2.getChildren().add(node1);
                    break;
                }
            }
            if (!mark) {
                tree.add(node1);
            }
        }
        return tree;
    }


    /**
     * 创建
     */
    public Permission save(Map<String, String[]> params) {
        Permission permission = new Permission();
        return this.save(permission, params);
    }

    /**
     * 根据Id更新
     *
     * @param id
     * @param params
     */
    public Permission save(Integer id, Map<String, String[]> params) {

        //先查找对应记录
        Permission permission = repository.findOne(id);
        return this.save(permission, params);

    }

    public Permission save(Permission permission, Map<String, String[]> params) {

        //初始化BeanWrapper
        BeanWrapper bw = new BeanWrapperImpl(permission);

        //根据params对需要更新的值做处理，即动态调用对应的setter方法
        for (Map.Entry<String, String[]> entry : params.entrySet()) {
            //只要有传参数进来，就认为修改该属性
            bw.setPropertyValue(entry.getKey(), entry.getValue());
        }
        //把实体类的值填充了，才能再做下一步处理。

        //同pid下重名判断及path处理
        if (params.containsKey("parentId")) {
            //判断同ID下text是否有重复
            int count = 0;
            if (permission.getId() == null) {
                // 创建时
                count = repository.countByParentIdAndText(permission.getParentId(), permission.getText());
            } else {
                // 修改时
                count = repository.countByParentIdAndTextAndIdNot(permission.getParentId(), permission.getText(), permission.getId());
            }

            if (count > 0) {
                throw new AppException("权限名称重复");
            }

            //获取path
            if (permission.getParentId() > 1) {
                Permission parentPermission = repository.findOne(permission.getParentId());
                permission.setPath(parentPermission.getPath() + "," + permission.getParentId());
            } else {
                permission.setPath("1");
            }
        }
        return repository.save(permission);
    }

    public List<Permission> getTree() {
        List<Permission> permissions = repository.findByDeleted(0);

        List<Permission> tree = new ArrayList<Permission>();
        for (Permission node1 : permissions) {
            boolean mark = false;
            for (Permission node2 : permissions) {
                if (node1.getParentId() != null && node1.getParentId().equals(node2.getId())) {
                    mark = true;
                    if (node2.getChildren() == null)
                        node2.setChildren(new ArrayList<Object>());
                    node2.getChildren().add(node1);
                    break;
                }
            }
            if (!mark) {
                tree.add(node1);
            }
        }
        return tree;
    }

    public Permission get(Integer id) {
        return repository.findOne(id);
    }
}
