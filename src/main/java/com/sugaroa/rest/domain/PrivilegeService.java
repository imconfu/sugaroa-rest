package com.sugaroa.rest.domain;

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
    public void create() {
        Privilege entity = new Privilege();
        repository.save(entity);
    }

    /**
     * 根据Id更新
     *
     * @param id
     * @param params
     */
    public void update(Integer id, Map<String, String[]> params) {
        //先查找对应记录
        Privilege privilege = repository.findOne(id);

        //初始化BeanWrapper
        BeanWrapper bw = new BeanWrapperImpl(privilege);

        //根据params对需要更新的值做处理，即动态调用对应的setter方法
        for (Map.Entry<String, String[]> entry : params.entrySet()) {
            //只要有传参数进来，就认为修改该属性
            bw.setPropertyValue(entry.getKey(), entry.getValue());
        }

        //TODO 对一些值做预处理，如改变了pid则对应改变path值

        repository.save(privilege);
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
