package com.sugaroa.rest.web;

import com.sugaroa.rest.domain.Role;
import com.sugaroa.rest.domain.User;
import com.sugaroa.rest.service.MenuService;
import com.sugaroa.rest.service.RoleService;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;

@RestController
@Validated
public class RoleController {

    @Resource
    private RoleService service;

    @RequestMapping(value = "/roles/{id}")
    public Role get(@PathVariable Integer id) {
        return service.get(id);
    }
}
