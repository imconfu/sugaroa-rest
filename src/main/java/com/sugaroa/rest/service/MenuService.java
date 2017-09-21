package com.sugaroa.rest.service;

import com.sugaroa.rest.domain.*;
import com.sugaroa.rest.domain.Menu;
import com.sugaroa.rest.exception.AppException;
import org.apache.commons.beanutils.BeanUtils;
import org.springframework.beans.BeanWrapper;
import org.springframework.beans.BeanWrapperImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.lang.reflect.InvocationTargetException;
import java.util.*;

@Service
public class MenuService {
    private final MenuRepository repository;

    @Autowired
    public MenuService(MenuRepository repository) {
        this.repository = repository;
    }


    public Menu get(Integer id) {
        return repository.findOne(id);
    }

    /**
     * 获得树形结果
     *
     * @return
     */
    public List<Menu> getTree() {
        List<Menu> menus = repository.findByDeleted(0);

        List<Menu> tree = new ArrayList<Menu>();
        for (Menu node1 : menus) {
            boolean mark = false;
            for (Menu node2 : menus) {
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
     * 获取树形结构
     *
     * @return
     */
    public List<SimpleTree> getComboTree() {

        List<Menu> menus = repository.findByEnabledAndDeleted(1, 0);

        List<SimpleTree> tree = new ArrayList<SimpleTree>();
        for (SimpleTree node1 : menus) {
            boolean mark = false;
            for (SimpleTree node2 : menus) {
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

    private boolean isEnabled(Set<Integer> menuPriv, Set<Integer> userPriv) {
        // 用户拥有所有权限
        if (userPriv != null && userPriv.contains(new Integer(1))) {
            return true;
        }

        // 菜单未定义关联权限
        if (menuPriv == null || menuPriv.size() == 0) return true;

        // 用户未定义权限
        if (userPriv == null || userPriv.size() == 0) return false;

        // 计算交集判断权限
        Set<Integer> result = new HashSet<Integer>();
        result.addAll(userPriv);
        result.retainAll(menuPriv);
        if (result.size() > 0) {
            return true;
        }
        return false;
    }

    /**
     * 根据用户权限获得对应菜单
     *
     * @param userId
     * @return
     */
    public List<Menu> getByCurrentUser(int userId) {
        List<Menu> menus = repository.findByEnabledAndDeleted(1, 0);

        //先判断用户是否有菜单对应权限，对菜单做处理
//        Iterator<Menu> it = menus.iterator();
//        while (it.hasNext()) {
//            Menu menu = it.next();
//            //判断用户是否有该叶子(href有值)菜单权限
//            Set<Integer> menuPriv = menu.getPrivilegeArray();
//            if (menuPriv != null) {
//                if (!menu.getHref().isEmpty() && !isEnabled(menuPriv, userPriv)) {
//                    //用户没有该叶子菜单权限
//                    System.out.println(menu.getId() + "用户没有该叶子菜单权限");
//                    it.remove();    //循环删除，要使用it.remove，不能用menu.remove，否则会报ConcurrentModificationException
//                    continue;
//                }
//                //设置为null，接口不返回该值
//                menu.setPrivilegeArray(null);
//            }
//        }

        //再转为树形结构
        List<Menu> tree = new ArrayList<Menu>();
        for (Menu node1 : menus) {
            boolean mark = false;
            for (Menu node2 : menus) {
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
     * 获取id=>text键值对
     *
     * @return
     */
    public Map<Integer, String> getPairs() {
        Map<Integer, String> result = new HashMap<Integer, String>();

        List<Menu> menus = repository.findByEnabledAndDeleted(1, 0);
        for (SimpleTree menu : menus) {
            result.put(menu.getId(), menu.getText());
        }
        return result;
    }

    /**
     * 创建
     */
    public Menu save(Map<String, Object> params) {
        Menu menu = new Menu();
        return this.save(menu, params);
    }

    /**
     * 根据Id更新
     *
     * @param id
     * @param params
     */
    public Menu save(Integer id, Map<String, Object> params) {

        //先查找对应记录
        Menu menu = repository.findOne(id);
        return this.save(menu, params);

    }

    public Menu save(Menu menu, Map<String, Object> params) {

        //初始化BeanWrapper
        BeanWrapper bw = new BeanWrapperImpl(menu);

        //根据params对需要更新的值做处理，即动态调用对应的setter方法
        for (Map.Entry<String, Object> entry : params.entrySet()) {
            ////有设置关联权限
            if (entry.getKey().equals("permissions")) {
                menu.getPermissions().clear();
                List<Integer> permissions = (List<Integer>)entry.getValue();
                for(Integer id : permissions) {
                    Permission p = new Permission();
                    p.setId(id);
                    menu.getPermissions().add(p);
                }
            } else {
                //只要有传参数进来，就认为修改该属性
                bw.setPropertyValue(entry.getKey(), entry.getValue());
            }
        }
        //把实体类的值填充了，才能再做下一步处理。

        //同parentId下重名判断及path处理
        if (params.containsKey("parentId")) {
            //判断同ID下text是否有重复
            int count = 0;
            if (menu.getId() == null) {
                // 创建时
                count = repository.countByParentIdAndText(menu.getParentId(), menu.getText());
            } else {
                // 修改时
                count = repository.countByParentIdAndTextAndIdNot(menu.getParentId(), menu.getText(), menu.getId());
            }

            if (count > 0) {
                throw new AppException("菜单名称重复");
            }

            //获取path
            if (menu.getParentId() > 1) {
                Menu parentMenu = repository.findOne(menu.getParentId());
                menu.setPath(parentMenu.getPath() + "," + menu.getParentId());
            } else {
                menu.setPath("1");
            }
        }
        return repository.save(menu);
    }
}
