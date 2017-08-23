package com.sugaroa.rest.web;

import com.sugaroa.rest.domain.Menu;
import com.sugaroa.rest.domain.MenuService;
import com.sugaroa.rest.domain.User;
import org.json.JSONException;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.RequestAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;


@RestController
@PreAuthorize("hasRole('ADMIN')")
public class MenuController {

    private MenuService service;

    public MenuController(MenuService service) {
        this.service = service;
    }

    @RequestMapping("/menu/getusermenu")
    List<Menu> getUserMenu(@RequestAttribute("user") User user) throws JSONException {
        System.out.println("Request中的用户信息");
        System.out.println(user.getAccount());

        return service.getUserMenu(user.getId());
    }
}
