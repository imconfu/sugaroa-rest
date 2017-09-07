package com.sugaroa.rest.service;

import com.sugaroa.rest.domain.Privilege;
import com.sugaroa.rest.domain.PrivilegeRepository;
import com.sugaroa.rest.exception.AppException;
import org.springframework.beans.BeanWrapper;
import org.springframework.beans.BeanWrapperImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import com.sugaroa.rest.domain.SimpleTree;

import java.util.*;

@Service
public class PrivilegeService {
    public static final int MAX_OPERATOR = 2147483647;
    public static final String ALL_RESOURCE = "ALL";

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
                    privilege.setOperator(MAX_OPERATOR);
                } else {
                    int operator = lastPrivilege.getOperator();
                    privilege.setOperator((operator == MAX_OPERATOR ? 1 : operator * 2));
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
        List<Privilege> privileges = repository.findByDeleted(0);

        List<Privilege> tree = new ArrayList<Privilege>();
        for (Privilege node1 : privileges) {
            boolean mark = false;
            for (Privilege node2 : privileges) {
                if (node1.getPid() != null && node1.getPid().equals(node2.getId())) {
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

    public Privilege get(Integer id) {
        return repository.findOne(id);
    }

    /**
     * 获取树形结构
     *
     * @return
     */
    public List<SimpleTree> getComboTree() {

        List<SimpleTree> privileges = repository.findByStatusAndDeleted(1, 0);

        List<SimpleTree> tree = new ArrayList<SimpleTree>();
        for (SimpleTree node1 : privileges) {
            boolean mark = false;
            for (SimpleTree node2 : privileges) {
                if (node1.getPid() != null && node1.getPid().equals(node2.getId())) {
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
     * 获取id=>text键值对
     *
     * @return
     */
    public Map<Integer, String> getPairs() {
        Map<Integer, String> result = new HashMap<Integer, String>();

        List<SimpleTree> privileges = repository.findByStatusAndDeleted(1, 0);
        for (SimpleTree privilege : privileges) {
            result.put(privilege.getId(), privilege.getText());
        }
        return result;
    }

    /**
     * 根据origin权限解析关联权限，生成最终权限list和object
     *
     * @param origin
     * @param object
     * @param list
     */
    public void parse(String origin, Map<String, Integer> object, Set<Integer> list) {
        Set<Integer> privilegeList = new HashSet<Integer>();

        String[] privilegeArray = origin.split(",");
        if(!origin.isEmpty() && privilegeArray.length > 0){
            for (String id : privilegeArray) {
                privilegeList.add(Integer.valueOf(id));
            }
            this.parse(privilegeList, object, list);
        }
    }

    public void parse(Set<Integer> origin, Map<String, Integer> object, Set<Integer> list) {
        System.out.println("PrivilegeService.parse Run！");
        // 首次进入判断是否拥有所有权限
        if (object.size() == 0 && list.size() == 0) {
            Integer all = new Integer(1);
            if (origin.contains(all)) {
                list.add(all);
                object.put(ALL_RESOURCE, MAX_OPERATOR);
                System.out.println("PrivilegeService.parse 拥有所有权限！");
                return;
            }
        }
        Set<Integer> relation = new HashSet<Integer>();
        List<Privilege> privileges = repository.findByIdInAndStatusAndDeleted(origin, 1, 0);

        for (Privilege privilege : privileges) {
            String resource = privilege.getResource();
            Integer operator = privilege.getOperator();

            if (resource.isEmpty()) continue;

            list.add(privilege.getId());
            if (!object.containsKey(resource)) {
                //resource没有在object中，则先初始化
                object.put(resource, 0);
            }
            if (operator == MAX_OPERATOR) {
                object.put(resource, MAX_OPERATOR);
            }
            // TODO 可能会有问题 Integer运算
            if (operator > 0 && operator < MAX_OPERATOR && (operator & object.get(resource)) == 0) {
                object.put(resource, object.get(resource) + operator);
            }

            List<Integer> currRelation = privilege.getRelation();
            if (currRelation != null) {
                //合并
                relation.addAll(currRelation);
            }
        }

        System.out.println("relation");
        for (Integer id : relation) {
            System.out.print(id+",");
        }
        System.out.println("");

        //找出不在最终权限列表list中的关联权限,即差集
        Set<Integer> recursion = new HashSet<Integer>();
        recursion.addAll(relation);
        recursion.removeAll(list);

        //将relaction合并到list中
        list.addAll(relation);

        if (recursion.size() > 0) {
            parse(recursion, object, list);
        }
    }
}
