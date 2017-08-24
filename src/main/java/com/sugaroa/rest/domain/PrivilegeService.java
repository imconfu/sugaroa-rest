package com.sugaroa.rest.domain;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import com.sugaroa.rest.SimpleTree;

import java.util.ArrayList;
import java.util.List;

@Service
public class PrivilegeService {

    private final PrivilegeRepository repository;

    @Autowired
    public PrivilegeService(PrivilegeRepository repository) {
        this.repository = repository;
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
}
