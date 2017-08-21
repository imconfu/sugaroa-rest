package com.sugaroa.rest.web;

import com.sugaroa.rest.Result;
import com.sugaroa.rest.domain.Menu;
import com.sugaroa.rest.domain.MenuRepository;
import com.sugaroa.rest.domain.MenuService;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
public class MenuController {
    private MenuService service;

    public MenuController(MenuService service) {
        this.service = service;
    }

    @RequestMapping("/menu/getusermenu")
    Result getUserMenu() {
        return new Result("menuTree", service.getUserMenu());
    }
}
