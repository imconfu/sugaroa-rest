package com.sugaroa.rest.web;

import com.sugaroa.rest.domain.Role;
import com.sugaroa.rest.domain.SimpleTree;
import com.sugaroa.rest.domain.User;
import com.sugaroa.rest.service.MenuService;
import com.sugaroa.rest.service.RoleService;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import javax.validation.constraints.Min;
import java.util.List;
import java.util.Map;

@RestController
@Validated
public class RoleController {

    @Resource
    private RoleService service;

    @RequestMapping("/roles")
    List<Role> getAll() {
        return service.getAll();
    }

    @RequestMapping(value = "/roles/{id}")
    public Role get(@PathVariable Integer id) {
        return service.get(id);
    }

    @RequestMapping(value = "/roles/{id}", method = RequestMethod.POST)
    public Role update(@Min(value = 1, message = "菜单ID必须大于1") @PathVariable Integer id, @RequestBody Map<String, Object> params) {
        return service.save(id, params);
    }
    @RequestMapping(value = "/roles", method = RequestMethod.POST)
    public Role create(@RequestBody  Map<String, Object> params) {
        return service.save(params);
    }

    @RequestMapping("/roles/pairs")
    Map<Integer, String> getPairs() {
        return service.getPairs();
    }

    @RequestMapping("/roles/simplelist")
    List<Role> getSimpleList() {
        return service.getSimpleList();
    }
}
