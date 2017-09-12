package com.sugaroa.rest.web;

import com.sugaroa.rest.domain.Menu;
import com.sugaroa.rest.domain.SimpleTree;
import com.sugaroa.rest.service.MenuService;
import com.sugaroa.rest.domain.User;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import javax.validation.constraints.Min;
import java.util.List;
import java.util.Map;

@RestController
@Validated
public class MenuController {

    private MenuService service;

    public MenuController(MenuService service) {
        this.service = service;
    }

    @RequestMapping("/menus")
    List<Menu> getTree() {
        return service.getTree();
    }

    @RequestMapping(value = "/menus/{id}")
    public Menu get(@Min(value = 99, message = "菜单ID必须大于99") @PathVariable Integer id) {
        return service.get(id);
    }

    @RequestMapping(value = "/menus", method = RequestMethod.POST)
    public Menu create(HttpServletRequest request) {
        return service.save(request.getParameterMap());
    }

    @RequestMapping(value = "/menus/{id}", method = RequestMethod.POST)
    public Menu update(@Min(value = 1, message = "菜单ID必须大于1") @PathVariable Integer id, HttpServletRequest request) {
        return service.save(id, request.getParameterMap());
    }

    @RequestMapping("/menus/combotree")
    List<SimpleTree> getComboTree() {
        return service.getComboTree();
    }

    @RequestMapping("/menus/pairs")
    Map<Integer, String> getPairs() {
        return service.getPairs();
    }

    @RequestMapping("/menus/currentuser")
    List<Menu> getByCurrentUser(@RequestAttribute("user") User user) {
        System.out.println("/menus/currentuser");
        //return service.getByCurrentUser(user.getId(), user.getPrivilegeArray());
        return null;
    }

}
