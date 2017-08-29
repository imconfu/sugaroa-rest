package com.sugaroa.rest.domain;

import com.sugaroa.rest.exception.AppException;
import org.springframework.beans.BeanWrapper;
import org.springframework.beans.BeanWrapperImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import com.sugaroa.rest.SimpleTree;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
public class PrivilegeService {

    private final PrivilegeRepository repository;

    @Autowired
    public PrivilegeService(PrivilegeRepository repository) {
        this.repository = repository;
    }

    /**
     * 创建
     */
    public Privilege save(Map<String, String[]> params) {
        Privilege privilege = new Privilege();
        return this.save(privilege, params);
    }

    /**
     * 根据Id更新
     *
     * @param id
     * @param params
     */
    public Privilege save(Integer id, Map<String, String[]> params) {

        //先查找对应记录
        Privilege privilege = repository.findOne(id);
        return this.save(privilege, params);

    }

    public Privilege save(Privilege privilege, Map<String, String[]> params) {

        //初始化BeanWrapper
        BeanWrapper bw = new BeanWrapperImpl(privilege);

        //根据params对需要更新的值做处理，即动态调用对应的setter方法
        for (Map.Entry<String, String[]> entry : params.entrySet()) {
            //只要有传参数进来，就认为修改该属性
            bw.setPropertyValue(entry.getKey(), entry.getValue());
        }
        //把实体类的值填充了，才能再做下一步处理。

        //同pid下重名判断及path处理
        if (params.containsKey("pid")) {
            //判断同ID下text是否有重复
            int count = 0;
            if (privilege.getId() == null) {
                // 创建时
                count = repository.countByPidAndText(privilege.getPid(), privilege.getText());
            } else {
                // 修改时
                count = repository.countByPidAndTextAndIdNot(privilege.getPid(), privilege.getText(), privilege.getId());
            }

            if (count > 0) {
                String message = "分组名称重复";
                // TODO 可以细化提示信息：资源/操作
                throw new AppException("权限名称重复");
            }

            //获取path
            if (privilege.getPid() > 1) {
                Privilege parentPrivilege = repository.findOne(privilege.getPid());
                privilege.setPath(parentPrivilege.getPath() + "," + privilege.getPid());
            } else {
                privilege.setPath("1");
            }
        }

        //有设置resource的值处理operator值，只有新增加时才有效
        if (privilege.getId() == null) {
            if (params.containsKey("resource")) {
                //获取该resource下最后一条数据，获取operator值，进而生成下一个operator值
                Privilege lastPrivilege = repository.findTopByResourceOrderByIdDesc(privilege.getResource());
                if (lastPrivilege == null) {
                    privilege.setOperator(2147483647);
                } else {
                    int operator = lastPrivilege.getOperator();
                    privilege.setOperator((operator == 2147483647 ? 1 : operator * 2));
                }
            } else {
                privilege.setOperator(0);
            }
        }

        return repository.save(privilege);
    }

    /**
     * 获得树形结果
     *
     * @return
     */
    public List<Privilege> getTree() {
        List<Privilege> Authorities = repository.findByDeleted(0);

        List<Privilege> tree = new ArrayList<Privilege>();
        for (Privilege node1 : Authorities) {
            boolean mark = false;
            for (Privilege node2 : Authorities) {
                if (node1.getPid() != null && node1.getPid() == node2.getId()) {
                    mark = true;
                    if (node2.getChildren() == null)
                        node2.setChildren(new ArrayList<Privilege>());
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

    public Privilege findById(Integer id) {
        return repository.findOne(id);
    }

    /**
     * 获取树形结构
     *
     * @return
     */
    public List<SimpleTree> getComboTree() {

        List<SimpleTree> Privileges = repository.findByStatusAndDeleted(1, 0);

        List<SimpleTree> tree = new ArrayList<SimpleTree>();
        for (SimpleTree node1 : Privileges) {
            boolean mark = false;
            for (SimpleTree node2 : Privileges) {
                if (node1.getPid() != null && node1.getPid() == node2.getId()) {
                    mark = true;
                    if (node2.getChildren() == null)
                        node2.setChildren(new ArrayList<SimpleTree>());
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
     * 获取id=>text键值对
     *
     * @return
     */
    public Map<Integer, String> getKeyValuePairs() {
        Map<Integer, String> result = new HashMap<Integer, String>();

        List<SimpleTree> Privileges = repository.findByStatusAndDeleted(1, 0);
        for (SimpleTree privilege : Privileges) {
            result.put(privilege.getId(), privilege.getText());
        }
        return result;
    }
}
