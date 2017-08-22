package com.sugaroa.rest.domain;

import org.json.JSONException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

@Service
public class AuthorityService {
    private final AuthorityRepository repository;

    @Autowired
    public AuthorityService(AuthorityRepository repository) {
        this.repository = repository;
    }

    /**
     * 获得树形结果
     *
     * @return
     */
    public List<Authority> getTree() {
        List<Authority> Authorities = repository.findByDeleted(0);

        List<Authority> tree = new ArrayList<Authority>();
        for (Authority node1 : Authorities) {
            boolean mark = false;
            for (Authority node2 : Authorities) {
                if (node1.getPid() != null && node1.getPid() == node2.getId()) {
                    mark = true;
                    if (node2.getChildren() == null)
                        node2.setChildren(new ArrayList<Authority>());
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
