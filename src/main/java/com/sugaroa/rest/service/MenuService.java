package com.sugaroa.rest.service;

import com.sugaroa.rest.domain.Menu;
import com.sugaroa.rest.domain.MenuRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

@Service
public class MenuService {
    private final MenuRepository repository;

    @Autowired
    public MenuService(MenuRepository repository) {
        this.repository = repository;
    }

    boolean checkUserMenuPurview(Map<String, Integer> userPurview, Map<String, Integer> menuPurview) {
        // 用户拥有所有权限
        if (userPurview.getOrDefault("ALL", 0).equals(2147483647)) {
            return true;
        }

        // 菜单未定义关联权限
        if (menuPurview == null || menuPurview.size() == 0) return true;

        // 用户未定义权限
        if (userPurview == null || userPurview.size() == 0) return false;

        //遍历判断权限
        for (String key : menuPurview.keySet()) {
            //menuPurview.get(key)
            System.out.println("Key = " + key);

        }
        return false;
    }

    /**
     * 根据用户权限获得对应菜单
     *
     * @param userId
     * @return
     */
    public List<Menu> getUserMenu(int userId) {
        List<Menu> menus = repository.findByStatusAndDeleted(1, 0);

        List<Menu> tree = new ArrayList<Menu>();
        for (Menu node1 : menus) {
            boolean mark = false;
            for (Menu node2 : menus) {
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
}
