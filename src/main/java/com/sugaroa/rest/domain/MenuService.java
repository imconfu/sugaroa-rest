package com.sugaroa.rest.domain;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
public class MenuService {
    private final MenuRepository repository;

    @Autowired
    public MenuService(MenuRepository repository) {
        this.repository = repository;
    }

    public List<Menu> getUserMenu() {
        List<Menu> menus = repository.findByStatusAndDeleted(1, 0);
        for (Menu menu : menus) {
            System.out.print("location 1:" + menu.getId());
            System.out.print("location 2:" + menu.getText());
            System.out.print("location 3:" + menu.getStatus());
        }

        List<Menu> nodeList = new ArrayList<Menu>();
        for (Menu node1 : menus) {
            boolean mark = false;
            for (Menu node2 : menus) {
                if (node1.getPid() != null && node1.getPid() == node2.getId()) {
                    mark = true;
                    if (node2.getChildren() == null)
                        node2.setChildren(new ArrayList<Menu>());
                    node2.getChildren().add(node1);
                    break;
                }
            }
            if (!mark) {
                nodeList.add(node1);
            }
        }
        return nodeList;
    }
}
